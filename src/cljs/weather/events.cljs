(ns weather.events
  (:require
    [ajax.core :as ajax]
    [clojure.string :refer [blank?] :as string]
    [day8.re-frame.http-fx]
    [weather.db :as db]
    [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]))

(defn init-state [db _]
  (let [cities [4930956 2643743 5368361]]
    ()))

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

(reg-event-db :weather/init-state init-state)
(reg-event-db :weather/sort-city sort-by-city)
