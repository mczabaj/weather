(ns weather.subs
  (:require [weather.db :as db]
            [re-frame.core :refer [dispatch reg-sub]]))

;;subscriptions

(reg-sub :page
  (fn [db _] (:page db)))

(reg-sub :docs
  (fn [db _] (:docs db)))

(reg-sub :weather/cities
  (fn [db _]
    (let [col (get-in db [:sort :sort-val])
          sorted-cities (sort-by col (get db :cities []))]
      (if (get-in db [:sort :ascending])
        sorted-cities
        (rseq sorted-cities)))))

(reg-sub :forecast/city-id
  (fn [db _] (get-in db [:forecast :city-id])))

(reg-sub :forecast/city-name
  (fn [db _] (get-in db [:forecast :city-name] "No City Selected")))

(reg-sub :forecast/weather
  (fn [db _] (get-in db [:forecast :weather] [])))
