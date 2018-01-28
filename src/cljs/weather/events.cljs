(ns weather.events
  (:require
    [ajax.core :as ajax]
    [clojure.string :refer [blank? join] :as string]
    [day8.re-frame.http-fx]
    [weather.db :as db]
    [reagent.core :as r]
    [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]
    [cljs-http.client :as http]
    [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def appid "f333c36f17612d7b693745b00991425a")

(defn make-remote-call [db endpoint success-event-kw failure-event-kw]
  ;(println "3. going to try to make the call out for weather:" endpoint)
  {:db db
   :http-xhrio {:method          :get
                :uri             endpoint
                :timeout         5000
                :format          (ajax/json-request-format)
                :response-format (ajax/json-response-format {:keywords? true})
                :on-success      [success-event-kw]
                :on-failure      [failure-event-kw]}})

;; http://api.openweathermap.org/data/2.5/group?id=4930956,2643743,5368361&appid=f333c36f17612d7b693745b00991425a&units=imperial
(defn make-weather-url [ids]
  (let [main         "http://api.openweathermap.org/data/2.5/group?id="
        city-ids     (join "," ids)
        other-params (str "&appid=" appid "&units=imperial")]
    ;(println "2. made the url:" main city-ids other-params)
    (str main city-ids other-params)))

;; http://api.openweathermap.org/data/2.5/forecast?id=4930956&appid=f333c36f17612d7b693745b00991425a&units=imperial
(defn make-forecast-url [id]
  (let [main         "http://api.openweathermap.org/data/2.5/forecast?id="
        other-params (str "&appid=" appid "&units=imperial")]
    (str main id other-params)))

(defn get-current [{:keys [db]} [_ _]]
  (let [city-ids (:default-cities db)
        url (make-weather-url city-ids)]
    ;(println "1. the cities in the DB:" (:cities db))
    (make-remote-call db
                      url
                      :weather/get-current-success
                      :weather/get-current-failure)))

(defn get-current-success [db [_ response]]
  ;(println "4S. get data success whole response:" response)
  (let [cities (r/atom [])
        body   (:list response)]
    ;(println "5. get data success, body resp:" body)
    (doseq [city body]
      (let [id   (:id city)
            name (:name city)
            temp (get-in city [:main :temp])]
        (swap! cities conj {:id id :name name :temp temp})))
    ;(println "6. new cities map:" @cities)
    (assoc db :cities @cities)))

(defn get-current-failure [db [_ response]]
  ;(println "4F. get data failure whole response:" response)
  (assoc db [:cities] {:msg "No Data Returned"}))

(defn sort-table [db [_ column]]
  (if (= column (get-in db [:sort :sort-val]))
    (assoc-in db [:sort :ascending] (not (get-in db [:sort :ascending])))
    (-> db
        (assoc-in [:sort :sort-val] column)
        (assoc-in [:sort :ascending] true))))

(defn set-city-id [{:keys [db]} [_ city-id]]
  {:db (assoc-in db [:forecast :city-id] city-id)
   :dispatch [:set-active-page :forecast]})

(defn get-5-day [{:keys [db]} [_ _]]
  (let [city-id (get-in db [:forecast :city-id])
        url     (make-forecast-url city-id)]
    (make-remote-call db
                      url
                      :forecast/get-5-day-success
                      :forecast/get-5-day-failure)))

(defn get-5-day-success [db [_ response]]
  (let [forecasts (r/atom [])
        hours-40  (:list response)]
    ;(println "5. get data success, body resp:" body)
    (doseq [hour hours-40]
      (let [date-time    (:dt_txt hour)
            weather-list (:description (first (:weather hour)))
            temp-min     (get-in hour [:main :temp_min])
            temp-max     (get-in hour [:main :temp_max])]
        (swap! forecasts conj {:date-time date-time
                               :weather-list weather-list
                               :temp-min temp-min
                               :temp-max temp-max})))
    ;(println "6. new cities map:" @cities)
    (-> db
        (assoc-in [:forecast :city-name] (get-in response [:city :name]))
        (assoc-in [:forecast :weather] @forecasts)
        (dissoc   [:forecast :msg]))))


(defn get-5-day-failure [db [_ response]]
  (-> db
      (assoc-in [:forecast :msg] "Failed to get 5-day forecast.")
      (dissoc [:forecast :weather])))

;;dispatchers

(reg-event-db :initialize-db   (fn [_ _] db/default-db))
(reg-event-db :set-active-page (fn [db [_ page]] (assoc db :page page)))
(reg-event-db :set-docs        (fn [db [_ docs]] (assoc db :docs docs)))

(reg-event-fx :weather/get-current         get-current)
(reg-event-db :weather/get-current-success get-current-success)
(reg-event-db :weather/get-current-failure get-current-failure)
(reg-event-db :weather/sort-table          sort-table)
(reg-event-fx :forecast/set-city-id        set-city-id)
(reg-event-fx :forecast/get-5-day          get-5-day)
(reg-event-db :forecast/get-5-day-success  get-5-day-success)
(reg-event-db :forecast/get-5-day-failure  get-5-day-failure)
