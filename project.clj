(defn get-path [folder-name paths]
  (->> paths
       (map (fn [dir]
              (str folder-name "/" dir "/src")))
       (vec)))

(defproject source-path-repro "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :source-paths ~(get-path "lib" ["feature"])
  :main feature.main)
