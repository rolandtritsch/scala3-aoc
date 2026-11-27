package aoc2016.day11

import scala.annotation.tailrec
import scala.collection.immutable.Queue

private val TopFloor = FloorCount - 1

def part1(input: Facility): Option[Int] =
  val initial = SearchState(0, canonicalPairs(input.pairs))
  search(Queue(initial -> 0), Set(initial))

@tailrec
private def search(queue: Queue[(SearchState, Int)], visited: Set[SearchState]): Option[Int] =
  // The FIFO queue preserves breadth-first order without repeated O(n) Seq appends.
  queue.dequeueOption match
    case None                              => None
    case Some(((state, steps), remaining)) =>
      if isGoal(state) then Some(steps)
      else
        val next = nextStates(state).distinct.filterNot(visited)
        search(remaining.enqueueAll(next.map(_ -> (steps + 1))), visited ++ next)
end search

private def nextStates(state: SearchState): Seq[SearchState] =
  val items = availableItems(state)
  val loads = items.map(Seq(_)) ++ items.indices.flatMap { first =>
    ((first + 1) until items.size).map(second => Seq(items(first), items(second)))
  }
  adjacentFloors(state.elevator)
    .flatMap(destination => loads.map(load => move(state, load, destination)).filter(isSafe))
end nextStates

private def availableItems(state: SearchState): Seq[ItemReference] = state.pairs.indices
  .flatMap { index =>
    val pair = state.pairs(index)
    Seq(
      Option
        .when(pair.generatorFloor == state.elevator)(ItemReference(index, ComponentKind.Generator)),
      Option
        .when(pair.microchipFloor == state.elevator)(ItemReference(index, ComponentKind.Microchip)),
    ).flatten
  }
end availableItems

private def adjacentFloors(floor: Int): Seq[Int] = Seq(floor - 1, floor + 1)
  .filter(next => next >= 0 && next < FloorCount)

private def move(state: SearchState, load: Seq[ItemReference], destination: Int): SearchState =
  val movedPairs = load.foldLeft(state.pairs) { (pairs, item) =>
    val pair = pairs(item.pairIndex)
    val moved = item.kind match
      case ComponentKind.Generator => pair.copy(generatorFloor = destination)
      case ComponentKind.Microchip => pair.copy(microchipFloor = destination)
    pairs.updated(item.pairIndex, moved)
  }
  SearchState(destination, canonicalPairs(movedPairs))
end move

private def isSafe(state: SearchState): Boolean =
  val generatorFloors = state.pairs.iterator.map(_.generatorFloor).toSet
  state.pairs.forall(pair =>
    pair.generatorFloor == pair.microchipFloor || !generatorFloors.contains(pair.microchipFloor)
  )

private def isGoal(state: SearchState): Boolean = state.pairs
  .forall(pair => pair.generatorFloor == TopFloor && pair.microchipFloor == TopFloor)
