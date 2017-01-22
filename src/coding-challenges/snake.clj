; Coding challenge 3 - https://www.youtube.com/watch?v=AaGK-fj-BAM
(ns quil-site.examples.nanoscopic
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def width 600)
(def height 600)
(def scale 20)
(def rows (/ width scale))
(def cols (/ height scale))

(defn rand-between [start end]
  (+ start (rand-int (- end start))))

(defn new-food-location []
  [(rand-int rows) (rand-int cols)])

(def dirs
  {:right [1 0]
   :up    [0 -1]
   :left  [-1 0]
   :down  [0 1]})

(defn new-snake []
  {:body [[(/ rows 2) (/ cols 2)]]
   :dir :right})

(defn add-points [& pts]
  (vec (apply map + pts)))

(defn move-snake
  [{:keys [body dir] :as snake} & grow]
  (assoc snake :body (cons (add-points (first body) (dir dirs))
                           (if grow body (butlast body)))))
(defn setup [] 
  (q/frame-rate 8)
  {:snake (new-snake)
   :food (new-food-location)
   :score 0})

(defn get-new-dir []
  (condp = (q/key-code)
    37 :left
    38 :up
    39 :right
    40 :down))

(defn update-state [{:keys [snake food score] :as state}]
  (cond
    (q/key-pressed?) 
      {:snake (assoc snake :dir (get-new-dir)) 
       :food food 
       :score score}
    (= (first (:body snake)) food)
      {:snake (move-snake snake :grow) 
       :food (new-food-location)
       :score (inc score)}
    :else
      {:snake (move-snake snake)
       :food food
       :score score}))

(defn draw-snake [snake]
  (q/fill 255)
  (doseq [[x y] (:body snake)]
    (q/rect (* x scale) (* y scale) scale scale)))

(defn draw-state [state] 
  (q/background 45)
  (let [[x y] (:food state)]
    (q/fill 252 153 25)
    (q/no-stroke)
    (q/rect (* x scale) (* y scale) scale scale))
  (draw-snake (:snake state))
  (q/text (str (:score state)) 10 10 100 100))

(q/defsketch nanoscopic
  :host "host"
  :size [width height]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])