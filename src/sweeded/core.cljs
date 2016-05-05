(ns sweeded.core
  (:require
    [sweeded.slides :refer [slides]]
    [dommy.core :as dommy :refer-macros [sel sel1]]
    [secretary.core :as secretary :refer-macros [defroute]]
    [clojure.string :as str]
    [markdown.core :as md]
    cljsjs.pixi
    [goog.events :as events]
    [goog.history.EventType :as EventType])
  (:import [goog.history Html5History EventType]))

(def current-page (atom 0))

(def slide-div (sel1 :#slide))
(def next-button (sel1 :#next-button))
(def prev-button (sel1 :#prev-button))

(defn draw-slide [n]
  (let [current-slide (get slides n)]
    (dommy/set-html! slide-div
                     (str "<h2>" (:title current-slide) "</h2>"
                          (md/md->html (:body current-slide))))))

(defn foobar [] nil)

(defn get-token []
  (str js/window.location.pathname js.window.location.hash))

(defn new-token [path]
  (str js/window.location.pathname path))

(defn get-path [] js/window.location.hash)

(secretary/set-config! :prefix "#")

(defroute home-path "/" []
  (reset! current-page 0)
  (draw-slide @current-page))

(defroute game-path "/game" []
  (js/console.log js/PIXI)
  (js/console.log (js/PIXI.autoDetectRenderer 256 256))
  (draw-slide @current-page))

(defroute page-path "/page/:n" [n]
  (let [page-n (js/parseInt n)]
    (reset! current-page page-n)
    (draw-slide page-n)))

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

(defn inc-or-reset [n]
  (let [next-n (inc n)]
    (if (= next-n (count slides))
      0
      next-n)))

(defn dec-or-reset [n]
  (if (= n 0)
    (dec (count slides))
    (dec n)))

(defn click-handler [e page-fn]
  (nav! (page-path {:n (page-fn @current-page)})))

(defn next-click-handler [e]
  (click-handler e inc-or-reset))

(defn prev-click-handler [e]
  (click-handler e dec-or-reset))

(dommy/listen! next-button
               :click next-click-handler)

(dommy/listen! prev-button
               :click prev-click-handler)

(nav! (game-path))
