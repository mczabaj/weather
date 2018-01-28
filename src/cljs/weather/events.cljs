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

(defn make-weather-call [{:keys [db]} [_ endpoint]]
  ;(println "3. going to try to make the call out for weather:" endpoint)
  {:db db
   :http-xhrio {:method          :get
                :uri             endpoint
                :timeout         5000
                :format          (ajax/json-request-format)
                :response-format (ajax/json-response-format {:keywords? true})
                :on-success      [:weather/get-data-success]
                :on-failure      [:weather/get-data-failure]}})

(defn make-url [ids]
  (let [main         "http://api.openweathermap.org/data/2.5/group?id="
        city-ids     (join "," ids)
        other-params (str "&appid=" appid "&units=imperial")]
    ;(println "2. made the url:" main city-ids other-params)
    (str main city-ids other-params)))

;; http://api.openweathermap.org/data/2.5/group?id=4930956,2643743,5368361&appid=f333c36f17612d7b693745b00991425a&units=imperial

(defn get-data [{:keys [db]} [_ ids]]
  ;(println "1. the cities in the DB:" (:cities db))
  (let [url (make-url ids)]
    {:db db
     :dispatch [:weather/make-weather-call url]}))

(defn get-data-success [db [_ response]]
  ;(println "4S. get data success whole response:" response)
  (let [cities (r/atom [])
        body   (:list response)]
        ;;name (:name response)
        ;;temp (get-in response [:main :temp])]
    ;(println "5. get data success, body resp:" body)
    (doseq [city body]
      (let [id   (:id city)
            name (:name city)
            temp (get-in city [:main :temp])]
        ;(println "5.5. inside for: " city)
        (swap! cities conj {:id id :name name :temp temp})))
    ;(println "6. new cities map:" @cities)
    (assoc db :cities @cities)))

(defn get-data-failure [db [_ response]]
  ;(println "4F. get data failure whole response:" response)
  (assoc db [:cities] {:msg "No Data Returned"}))

(defn init-state [{:keys [db]} [_ _]]
  ;(println "doing init state")
  (let [city-ids (:default-cities db)]
    {:db db
     :dispatch [:weather/get-data city-ids]}))

(defn sort-table [db [_ column]]
  (if (= column (get-in db [:sort :sort-val]))
    (assoc-in db [:sort :ascending] (not (get-in db [:sort :ascending])))
    (-> db
        (assoc-in [:sort :sort-val] column)
        (assoc-in [:sort :ascending] true))))

;;dispatchers

(reg-event-db :initialize-db   (fn [_ _] db/default-db))
(reg-event-db :set-active-page (fn [db [_ page]] (assoc db :page page)))
(reg-event-db :set-docs        (fn [db [_ docs]] (assoc db :docs docs)))

(reg-event-fx :weather/init-state       init-state)
(reg-event-db :weather/sort-table       sort-table)
(reg-event-fx :weather/get-data         get-data)
(reg-event-fx :weather/make-weather-call make-weather-call)
(reg-event-db :weather/get-data-success get-data-success)
(reg-event-db :weather/get-data-failure get-data-failure)
