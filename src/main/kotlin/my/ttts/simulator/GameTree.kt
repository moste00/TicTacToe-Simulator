package my.ttts.simulator

import my.ttts.board.Board

class GameTree {
    private enum class NodeMarking {NonTerminal, WinX, WinO, Draw}
    private data class Node(var board : Board,
                            var parentIdx : Int,
                            var childrenIdx : MutableList<Int>,
                            var nodeMarking : NodeMarking)

    private var root : Node? = null
    private var nodes: MutableList<Node> = mutableListOf()
    private var currentParents = ArrayDeque<Int>()
    fun beginState(b : Board) {
        val parent = if (currentParents.isEmpty()) -1
                     else                          currentParents.last()
        val newNode = Node(b,parent, mutableListOf(),NodeMarking.NonTerminal)

        nodes.add(newNode)
        if (parent != -1) nodes[parent].childrenIdx.add(nodes.size - 1)

        if (root == null) {
            root = newNode
        }
        currentParents.addLast(nodes.size - 1)
    }
    fun endState() {
        currentParents.removeLast()
    }

    fun markCurrentStateAsWin(m : Mark) {
        val nodeMarking = if (m == Mark.X) NodeMarking.WinX
                          else             NodeMarking.WinO
        nodes[currentParents.last()].nodeMarking = nodeMarking
    }

    fun markCurrentStateAsDraw() {
        nodes[currentParents.last()].nodeMarking = NodeMarking.Draw
    }
}