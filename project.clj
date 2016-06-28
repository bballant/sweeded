(defproject hello-seymore "0.1.0-SNAPSHOT"
  :dependencies [[markdown-clj "0.9.74"]
                 [prismatic/dommy "1.1.0"]
                 [secretary "1.2.3"]
                 [cljsjs/pixi "3.0.10-0"]
                 [cljsjs/papaparse "4.1.1-1"]
                 [reagent "0.6.0-rc"]
                 [org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.122"]]
  :plugins [[lein-figwheel "0.3.9"]]
  :clean-targets [:target-path "out"]
  :cljsbuild {
    :builds [{:id "dev"
              :source-paths ["src"]
              :figwheel true
              :compiler {:main "sweeded.router"
                         :asset-path "js/out"
                         :output-to "resources/public/js/main.js"
                         :output-dir "resources/public/js/out"}}]})
