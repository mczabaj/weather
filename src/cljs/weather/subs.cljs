(ns weather.subs
  (:require [weather.db :as db]
            [re-frame.core :refer [dispatch reg-sub]]))

;;subscriptions

(reg-sub :page
  (fn [db _] (:page db)))

(reg-sub :docs
  (fn [db _] (:docs db)))

(reg-sub :weather/cities
  (fn [db _] (:weather/cities db)))
