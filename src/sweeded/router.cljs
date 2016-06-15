(ns sweeded.router
  (:require
    [sweeded.csv :as csv]
    [sweeded.slide-util :as ssu]
    [secretary.core :as secretary :refer-macros [defroute]]
    cljsjs.pixi
    [goog.events :as events]
    [goog.history.EventType :as EventType])
  (:import [goog.history Html5History EventType]))

(def current-page (atom 0))

(defn get-token []
  (str js/window.location.pathname js.window.location.hash))

(defn new-token [path]
  (str js/window.location.pathname path))

(defn get-path [] js/window.location.hash)

(secretary/set-config! :prefix "#")

(defroute home-path "/" []
  (reset! current-page 0)
  (ssu/draw-slide @current-page))

(defroute game-path "/game" []
  (js/console.log js/PIXI)
  (js/console.log (js/PIXI.autoDetectRenderer 256 256))
  (ssu/draw-slide @current-page))

(defroute dork-path "/dork" []
  (ssu/draw-slide @current-page))

(defroute page-path "/page/:n" [n]
  (let [page-n (js/parseInt n)]
    (reset! current-page page-n)
    (ssu/draw-slide page-n)))

(defn make-history []
  (doto (Html5History.)
    (.setPathPrefix (str js/window.location.protocol
                         "//"
                         js/window.location.host))
    (.setUseFragment false)))

(defn handle-url-change [e]
  ;; log the event object to console for inspection
  (js/console.log e)
  ;; and let's see the token
  (js/console.log (str "Navigating: " (get-token)))
  ;; we are checking if this event is due to user action,
  ;; such as click a link, a back button, etc.
  ;; as opposed to programmatically setting the URL with the API
  (when-not (.-isNavigation e)
    ;; in this case, we're setting it
    (js/console.log "Token set programmatically")
    ;; let's scroll to the top to simulate a navigation
    (js/window.scrollTo 0 0))
  ;; dispatch on the token
  (js/console.log (str "Dispatching!!!!!!! " (get-path)))
  (secretary/dispatch! (get-path)))

(defonce history (doto (make-history)
                   (goog.events/listen EventType.NAVIGATE
                                       ;; wrap in a fn to allow live reloading
                                       #(handle-url-change %))
                   (.setEnabled true)))

(defn nav! [path]
  (.setToken history (new-token path)))

(defn foobar []
  (js/console.log "foobar"))
