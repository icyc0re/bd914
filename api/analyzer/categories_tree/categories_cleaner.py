import os

def cut_categories():
	ftot = open('foursquare.txt', 'r')
	fr = open('analyzed_categories.txt', 'r')
	fw = open('categories_structure.txt', 'w')

	categories_all = []
	line = ftot.readline()
	while line:
		categories_all.append((line.strip(), ftot.readline().strip()))
		line = ftot.readline()
		line = ftot.readline()

	ftot.close()

	categories_extracted = []
	for line in fr:
		categories_extracted.append(' '.join(line.split()[:-1]))
	fr.close()

	catcut = [c for c in categories_all if c[0] in categories_extracted]

	for c in catcut:
		fw.write(c[0])
		fw.write(',')
		fw.write(c[1])
		fw.write('\n')

	fw.close()

	# verify! (some analyzed categories are not in the foursquare structure)
	print("extracted")
	for c in categories_extracted:
		if c not in [k[0] for k in catcut]:
			print(c)


def main():
	cut_categories()


if __name__ == '__main__':
	main()