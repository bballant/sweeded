(ns sweeded.router
  (:require
    [sweeded.csv :as csv]
    [sweeded.state :as state]
    [reagent.core :as reagent]
    [secretary.core :as secretary :refer-macros [defroute]]
    [goog.events :as events]
    [goog.history.EventType :as EventType])
  (:import [goog.history Html5History EventType]))

(defn hook-browser-navigation! []
  (doto (Html5History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn app-routes []
  (secretary/set-config! :prefix "#")

  (defroute "/" []
    (swap! state/app-state assoc :page :home))

  (defroute "/about" []
    (swap! state/app-state assoc :page :about))

  (hook-browser-navigation!))

(defn home []
  [:div [:h1 "Home Page"]
   [:div.container
    [:div.row
     [:input {:type "file"
              :id "file-input"
              :on-change csv/read-single-file}]]
    [:div.row [csv/csv-component]]
    [:div.row
     [:a {:href "#/about"} "about page"]]]])

(defn about []
  [:div [:h1 "About Page"]
   [:a {:href "#/"} "home page"]])

(defmulti current-page #(@state/app-state :page))
(defmethod current-page :home []
  [home])
(defmethod current-page :about []
  [about])
(defmethod current-page :default []
  [:div ])

(defn ^:export main []
  (app-routes)
  (reagent/render [current-page]
                  (.getElementById js/document "app")))
