(ns advent-of-code-2018.five
  (:require [advent-of-code-2018.utils :as utils]
            [clojure.string :as string]))

(def alpha-letters
  (->> (range 97 (+ 97 26))
       (map #(str (char %)))))

(defn matching [letter-a letter-b]
  (and (not= letter-a letter-b)
       (= (string/lower-case letter-a)
          (string/lower-case letter-b))))

(defn react [chain]
  (-> (loop [chain chain
             new-chain []
             last-letter nil]
        (if-let [letter (first chain)]
          (cond
            (nil? last-letter) (recur (next chain) new-chain letter)
            (matching letter last-letter) (recur (next chain) new-chain nil)
            :else (recur (next chain) (conj new-chain last-letter) letter))
          (conj new-chain last-letter)))
      (string/join)))

(defn loop-until-finish-reacting [chain]
  (loop [chain chain
         current-count (count chain)]
    (let [post-chain (react chain)
          post-chain-count (count post-chain)]
      (if (not= current-count post-chain-count)
        (recur post-chain post-chain-count)
        post-chain))))

(defn remove-letter-and-react [letter chain]
  (println letter)
  (-> chain
      (string/replace letter "")
      (string/replace (string/upper-case letter) "")
      (loop-until-finish-reacting)))

(defn go-1 []
  (->> (utils/load-file-from-path "resources/5.txt")
       (#(do (println (count %)) %))
       (loop-until-finish-reacting)
       (count)))

(defn go-2 []
  (let [chain (utils/load-file-from-path "resources/5.txt")]
    (->> (pmap #(remove-letter-and-react % chain) alpha-letters)
         (map count)
         (apply min))))