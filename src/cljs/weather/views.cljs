(ns weather.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [ajax.core :refer [GET]]
            [weather.events]))

(defn table-header []
  [:thead
   [:tr
    [:th {:width "200"
          :on-click #(rf/dispatch [:weather/sort-table :name])}
      "City"]
    [:th {:width "200"
          :on-click #(rf/dispatch [:weather/sort-table :temp])}
      "Current Temperature"]]])

(defn city-rows [cities]
  [:tbody
   (for [c cities]
     [:tr {:key (:id c) :data-id (:id c)
           :on-click #(rf/dispatch [:forecast/set-city-id (:id c)])}
       [:td (str (:name c))]
       [:td (str (:temp c))]])])

(defn city-table []
  [:table
    [table-header]
    [city-rows @(rf/subscribe [:weather/cities])]])

(defn back-to-list []
  [:div.row
    [:button#back {:type "button"
                   :class "btn btn-primary"
                   :on-click #(rf/dispatch [:set-active-page :home])}
      "Return to City List"]])

(defn forecast-rows [forecast]
  [:tbody
    (for [day forecast]
      [:tr {:key (:date-time day)}
        [:td (:date-time day)]
        [:td (:description day)]
        [:td (:temp-min day)]
        [:td (:temp-max day)]])])

(defn forecast-table []
  [:table
    [:thead
      [:tr
        [:th "Date and Time"]
        [:th "Description"]
        [:th "Minimum Temperature"]
        [:th "Maximum Temperature"]]]
    [forecast-rows @(rf/subscribe [:forecast/weather])]])

(defn weather-page []
  (rf/dispatch [:weather/get-current])
  (fn []
    [:div.container
      [:h1 "Current Weather"]
      [city-table]]))

(defn forecast-page []
  (rf/dispatch [:forecast/get-5-day])
  (fn []
    [:div.container
      [:h1 (str "5 Day Forecast for " @(rf/subscribe [:forecast/city-name]))]
      [back-to-list]
      [forecast-table]]))
