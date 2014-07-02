(ns apples.core.desktop-launcher
  (:require [apples.core :refer :all])
  (:import [com.badlogic.gdx.backends.lwjgl LwjglApplication]
           [org.lwjgl.input Keyboard])
  (:gen-class))

(defn -main
  []
  (LwjglApplication. apples "apples" 700 600)
  (Keyboard/enableRepeatEvents true))
