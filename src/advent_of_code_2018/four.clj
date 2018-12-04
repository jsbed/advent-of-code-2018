(ns advent-of-code-2018.four
  (:require [advent-of-code-2018.utils :as utils]
            [clojure.string :as string]))

(defn parse-date [word]
  (->> (utils/get-regex-groups #"\[(\d+)-(\d+)-(\d+) (\d+):(\d+)\]" word)
       (map #(Integer/parseInt %))
       (apply clj-time.core/date-time)))

(defn find-guard [word]
  (->> (utils/get-regex-groups #"\[.+\] Guard #(\d+)" word)
       (first)
       (#(if (some? %) (Integer/parseInt %)))))

(defn update-guard-work [{minutes :minutes interval :interval :as work} guard from to]
  (let [diff (clj-time.core/in-minutes (clj-time.core/interval from to))]
    (if (some? work)
      (assoc work :minutes (dec (+ minutes diff))
                  :interval (conj interval [from to]))
      {:minutes  (dec diff)
       :interval [[from to]]
       :guard    guard})))

(defn find-most-sleepy-hour [interval-list]
  (println interval-list)
  (let [freqs (->> (map (fn [[from to]] (range (clj-time.core/minute from) (clj-time.core/minute to))) interval-list)
                   (reduce concat)
                   (frequencies))
        most-freq (->> (vals freqs)
                       (apply max))]
    (loop [freqs freqs]
      (let [[minute freq] (first freqs)]
        (if (= freq most-freq)
          {:min minute :freq freq}
          (recur (rest freqs)))))))

(defn find-most-sleepy-hour-with-guard [work-map]
  (println (first work-map))
  (map #(hash-map :most-worked (find-most-sleepy-hour (:interval %))
                  :guard (:guard %)) work-map))

(defn create-work-map [history]
  (println (count history))
  (loop [events history
         work-map {}
         current-guard nil
         start-sleep-time nil]
    (if-let [{:keys [date guard wakes-up falls-asleep word]} (first events)]
      (do #_(println guard wakes-up falls-asleep word)
        (cond
          (pos-int? guard) (recur (rest events) work-map guard start-sleep-time)
          (true? falls-asleep) (recur (rest events) work-map current-guard date)
          (true? wakes-up) (recur (rest events) (update work-map current-guard update-guard-work current-guard start-sleep-time date) current-guard nil)))
      (do (println "done") work-map))))

(defn go-1 []
  (->> (utils/load-file-lines "resources/4.txt" #"\r\n")
       (map #(hash-map :word %
                       :date (parse-date %)
                       :guard (find-guard %)
                       :wakes-up (string/includes? % "wakes up")
                       :falls-asleep (string/includes? % "falls asleep")))
       (sort-by :date)
       (create-work-map)
       (vals)
       (sort-by :minutes)
       (last)
       (:interval)
       (find-most-sleepy-hour)))

(defn go-2 []
  (->> (utils/load-file-lines "resources/4.txt" #"\r\n")
       (map #(hash-map :word %
                       :date (parse-date %)
                       :guard (find-guard %)
                       :wakes-up (string/includes? % "wakes up")
                       :falls-asleep (string/includes? % "falls asleep")))
       (sort-by :date)
       (create-work-map)
       (vals)
       (find-most-sleepy-hour-with-guard)))