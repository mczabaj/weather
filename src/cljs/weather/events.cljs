(ns weather.events
  (:require
    [clojure.string :refer [blank?] :as string]
    [day8.re-frame.http-fx]
    [weather.db :as db]
    [reagent.core :as r]
    [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]
    [cljs-http.client :as http]
    [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def appid "f333c36f17612d7b693745b00991425a")

(defn make-remote-call [endpoint]
  (go (let [response (<! (http/get endpoint {:with-credentials? false}))]
        (str (:body response)))))

(defn make-url [kw-id]
  (let [main    "http://api.openweathermap.org/data/2.5/weather"
        city-id (str "?id=" (name kw-id))
        app-id  (str "&appid=" appid)]
    (str main city-id app-id "&units=imperial")))

(defn get-data [db [_ kw-ids]]
  (println "the cities in the DB:" (:cities db))
  (let [cities (atom (:cities db))]
    (println "atom cities:" @cities)
    (for [id kw-ids]
      (let [url      (make-url id)
            response (make-remote-call url)
            name     (:name response)
            temp     (get-in response [:main :temp])]
        (println "the city name and temp" name "and" temp)
        (swap! cities assoc id {:name name :temp temp})))
    (println "updates cities:" @cities)
    (assoc db :cities @cities)))

(defn get-data-success [db [_ response]]
  (let [id (:id response)
        name (:name response)
        temp (get-in response [:main :temp])]
    (println "get data success, cities:" (:cities db))
    (assoc-in db [:cities id] {:name name
                               :temp temp})))
;
; (defn get-data-failure [db [_ response]])

(defn init-state [{:keys [db]} [_ _]]
  (println "doing init state")
  (let [city-ids (keys (:cities db))]
    {:db db
     :dispatch [:weather/get-data city-ids]}))

(defn sort-by-city
  [db [event _]])
  ;; get current sort order from db
  ;; resort db
  ;; update to new order

(defn sort-by-temp
  [db [event _]])

;;dispatchers

(reg-event-db :initialize-db   (fn [_ _] db/default-db))
(reg-event-db :set-active-page (fn [db [_ page]] (assoc db :page page)))
(reg-event-db :set-docs        (fn [db [_ docs]] (assoc db :docs docs)))

(reg-event-fx :weather/init-state       init-state)
(reg-event-db :weather/sort-city        sort-by-city)
(reg-event-db :weather/sort-temp        sort-by-temp)
(reg-event-db :weather/get-data         get-data)
(reg-event-db :weather/get-data-success get-data-success)
; (reg-event-db :weather/get-data-failure get-data-failure)
