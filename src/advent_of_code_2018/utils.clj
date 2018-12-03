(ns advent-of-code-2018.utils
  (:require [clojure.string :as string]))

(defn load-file-from-path [path]
  (slurp path))

(defn load-file-lines [path delimiter]
  (-> (load-file-from-path path)
      (string/split delimiter)))

(defn drop-nth [n coll]
  (keep-indexed #(if (not= %1 n) %2) coll))