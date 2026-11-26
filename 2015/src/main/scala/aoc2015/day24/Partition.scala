package aoc2015.day24

private[day24] def findCombinations(weights: Seq[Int], k: Int, target: Int): Seq[Seq[Int]] =
  if k == 0 then if target == 0 then Seq(Seq.empty) else Seq.empty
  else if weights.isEmpty then Seq.empty
  else if weights.length < k then Seq.empty
  else
    val head = weights.head
    val tail = weights.tail
    val withHead =
      if head <= target then findCombinations(tail, k - 1, target - head).map(head +: _)
      else Seq.empty
    val withoutHead = findCombinations(tail, k, target)
    withHead ++ withoutHead
end findCombinations

private[day24] def canPartition(weights: Seq[Int], target: Int, groups: Int): Boolean =
  if groups == 1 then weights.sum == target
  else if weights.isEmpty then false
  else
    val total = weights.sum
    if total != target * groups then false
    else
      (1 to weights.length).exists { k =>
        findCombinations(weights, k, target)
          .exists(combo => canPartition(weights.diff(combo), target, groups - 1))
      }
end canPartition
