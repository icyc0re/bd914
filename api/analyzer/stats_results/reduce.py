import re

class Tree(object):
	def __init__(self):
		self.tree = {}

	def add(self, obj):
		add = False
		for k,v in self.tree.items():
			gname = common(obj, k)
			if gname and len(v) > 1:
				if obj != gname and obj not in self.tree[gname]:
					self.tree[gname].append(obj)
				add = True
			elif gname and len(v) == 1:
				if obj != gname:
					del self.tree[k]
					self.tree[gname] = [v[0], obj]
				add = True
		if not add:
			self.tree[obj] = [obj]

	def merge(self, objs):
		if type(obj) == str:
			self.add(obj)
		elif type(obj) == list:
			for obj in objs:
				self.add(obj)

	def print(self):
		print("CATEGORIES: ")
		for k,v in self.tree.items():
			print("%s" % k)
			if len(v) > 1:
				for obj in v:
					print(" - %s" % obj)

	def verify(self):
		categ = []
		duplicates = []
		for k,v in self.tree.items():
			for obj in v:
				if obj in categ:
					duplicates.append(obj)
				else:
					categ.append(obj)
		return duplicates


def common(c1, c2):
	o1 = c1.split() if type(c1) == str else c1
	o2 = c2.split() if type(c2) == str else c2
	for w1 in o1:
		for w2 in o2:
			if w1 == w2 and re.compile('\w+').findall(w1):
				return w1
	return None

def main():
	categories = []
	fr = open('categories', 'r')
	for line in fr:
		elems = line.split()
		categories.append(' '.join(elems[:-1]))
	fr.close()
	
	tree = Tree()
	for c in categories:
		tree.add(c)
	
	tree.print()

	print(tree.verify())


if __name__ == '__main__':
	main()
