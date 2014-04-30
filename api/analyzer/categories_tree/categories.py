import math
import re

# PRECOMPUTED SIMILARITY MATRIX

UPSCALE_K = 0.5 # factor of reduction on parent/brother similarity (at each step)
DOWNSCALE_K = 0.95 # factor of reduction on descendent similarity (at each step)

categories_structure = []

class Node(object):
	def __init__(self, val, parent=None):
		self.parent = parent
		self.value = val
		self.children = []
		self.similarity = 0
	
	def get_value(self):
		return self.value

	def get_children(self):
		return self.children

	def get_parent(self):
		return self.parent

	def get_similarity(self):
		return self.similarity

	def add_child(self, node):
		child = Node(node, self)
		self.children.append(child)
		return child

	def depth(self):
		if self.parent is None:
			return 0
		else:
			return 1 + self.parent.depth()

	def list(self):
		nodes = [self]
		for c in self.children:
			nodes += c.list()
		return nodes

	def print_subtree(self, pre=''):
		print('%s%s' % (pre, self.value))
		for c in self.children:
			c.print_subtree('%s-' % pre)

	def compute(self, similarity, exception=None):
		""" Compute similarity for one node """
		self.similarity = similarity
		# if not root and parent is not the sender of the compute
		if self.parent is not None and self.parent is not exception:
			self.parent.compute(similarity * UPSCALE_K, self)
		for c in self.children:
			if c is not exception:
				c.compute(similarity * DOWNSCALE_K, self)



class Tree(object):
	def __init__(self, val):
		self.root = Node(val)

	def add(self, parent, node):
		return parent.add_child(node)

	def get_root(self):
		return self.root

	def find(self, val):
		return self.root.find(val)

	def list_nodes(self):
		return self.root.list()

	def print_structure(self):
		self.root.print_subtree()


def parse_forest(filename):
	""" Load categories tree from file """
	forest = []
	last_node = None
	tree = None

	fr = open(filename, 'r')
	for line in fr:
		d = len(re.compile('^-*').findall(line)[0])
		cline = line.lstrip('-').strip()

		if d == 0:
			forest.append(tree)
			tree = Tree(cline)
			last_node = tree.get_root()
		else:
			dold = last_node.depth()

			if d > dold:
				last_node = tree.add(last_node, cline)
			elif d == dold:
				last_node = tree.add(last_node.get_parent(), cline)
			elif d < dold:
				last_node = tree.add(last_node.get_parent().get_parent(), cline)
	
	forest.append(tree)
	fr.close()

	return forest[1:]


def generate_matrix(forest, filename):
	nodes = []
	for tree in forest:
		nodes += tree.list_nodes()
	
	fw = open(filename, 'w')
	for node in nodes:
		node.compute(1.0)

		for n in nodes[:-1]:
			fw.write('%f ' %n.get_similarity())
		fw.write('%f\n' % nodes[-1].get_similarity())

		node.compute(0.0)
	fw.close()




def main():
	forest = parse_forest('categories_structure.txt')
	generate_matrix(forest, 'categories_similarity.txt')


if __name__ == '__main__':
	main()