(defproject rest-people "0.1.0-SNAPSHOT"
  :description "A people catalog with a bit of a REST api."
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.6.1"]
                 [cheshire "5.8.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-mock "0.3.2"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [ring/ring-defaults "0.3.2"]]
  :main ^:skip-aot rest-people.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
