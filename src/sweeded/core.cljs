(ns sweeded.core
  (:require
    [dommy.core :as dommy :refer-macros [sel sel1]]
    [clojure.string :as str]
    [markdown.core :as md]))

(def message-in (sel1 :#message-in))
(def message-out (sel1 :#message-out))

(defn message-in->out![]
  (dommy/set-html! message-out
                   (-> (.-value message-in)
                       md/md->html)))

(defn grow-message-in! []
  (dommy/set-style! message-in :height "")
  (dommy/set-style! message-in :height (str (.-scrollHeight message-in) "px")))

(defn set-title! []
  (dommy/set-html! (sel1 :#title)
                   (-> (.-value message-in)
                       (str/split #"\n")
                       first
                       md/md->html)))

(defn message-in-change [e]
  (message-in->out!)
  (grow-message-in!)
  (set-title!))

(defn click-handler [e]
  (if (= "block" (dommy/style message-out "display"))
    (dommy/set-style! message-out :display "none")
    (dommy/set-style! message-out :display "block")))

(dommy/listen! message-in
               :input message-in-change
               :click click-handler)

(.log js/console "Hey")
