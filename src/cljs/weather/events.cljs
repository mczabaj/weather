(ns weather.events
  (:require
    [clojure.string :refer [blank?] :as string]
    [day8.re-frame.http-fx]
    [weather.db :as db]
    [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]
    [cljs-http.client :as http]
    [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def appid "f333c36f17612d7b693745b00991425a")

(defn make-remote-call [endpoint]
  (go (let [response (<! (http/get endpoint {:with-credentials? false}))]
        (:body response))))

(defn city-name [city-data]
  (println "getting the city name:" (:name city-data))
  (println "   from the city data:" city-data)
  (:name city-data))

(defn get-data [db [_ id]]
  (println "getting data for " id)
  (let [main    "http://api.openweathermap.org/data/2.5/weather"
        city-id (str "?id=" id)
        app-id  (str "&appid=" appid)
        url     (str main city-id app-id)
        city-data (make-remote-call url)
        city-name (city-name city-data)]
    (println "city data" city-data)
    (println "city name" city-name)
    (assoc-in db [:cities (keyword city-name)] city-data)))


(defn successful-get [db [_ data]]
  (println "successful get " data)
  (assoc-in db [:cities (keyword (city-name data))] data))

(defn failure [db _])

(defn init-state [{:keys [db]} [event _]]
  (println "doing init state")
  (let [cities [4930956 2643743 5368361]]
    (println "for each " (first cities))
    {:db db
     :dispatch [:weather/get-data (first cities)]}))

(defn sort-by-city
  [db [event _]])
  ;; get current sort order from db
  ;; resort db
  ;; update to new order

;;dispatchers

(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(reg-event-db
  :set-active-page
  (fn [db [_ page]]
    (assoc db :page page)))

(reg-event-db
  :set-docs
  (fn [db [_ docs]]
    (assoc db :docs docs)))

(reg-event-fx :weather/init-state     init-state)
(reg-event-db :weather/sort-city      sort-by-city)
(reg-event-db :weather/get-data       get-data)
(reg-event-db :weather/successful-get successful-get)
(reg-event-db :weather/failure        failure)
