package aoc2016.day02

enum Button(val label: Char, val row: Int, val column: Int):
  case One extends Button('1', 0, 0)
  case Two extends Button('2', 0, 1)
  case Three extends Button('3', 0, 2)
  case Four extends Button('4', 1, 0)
  case Five extends Button('5', 1, 1)
  case Six extends Button('6', 1, 2)
  case Seven extends Button('7', 2, 0)
  case Eight extends Button('8', 2, 1)
  case Nine extends Button('9', 2, 2)
