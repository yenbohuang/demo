# simple print
print(456)

# assign
a = 1
print('a=', a)
b = a
print('b=a=', b)

# type of
print('type of a', type(a))
print('type of 1', type(1))
print('type of 0.1', type(0.1))
print('type of string', type('string'))

# operators
print('floating-point division: 7 / 2 =', 7 / 2)
print('integer division: 7 // 2 =', 7 // 2)
print('7 % 2 =', 7 % 2)
print('Get both truncated quotient and remainder at once by divmod(): ', divmod(7, 2))
print('exponentiation: 3 ** 4 =', 3 ** 4)

# bases
print('Binary, 0b10 =', 0b10)
print('Octal, 0o10 =', 0o10)
print('Hex, 0x10 =', 0x10)

# type conversion
print('Convert True to int:', int(True))
print('Convert False to int:', int(False))
print('Convert 98.6 to int:', int(98.6))
print('Convert 1.0e4 to int:', int(1.0e4))
print('Convert string to int:', int('-23'))

print('Convert string to float:', float('-23.56'))

# string
print('''line 1
line 2
line 3''')

print('first ''second')
print('first ' + 'second')

print("'hello with single quote'")
print('\'hello with single quote\'')

print('Go ' * 4)
offsetTest = 'abcd'
print(offsetTest[0], offsetTest[-1])

print(len('0123456789'))

# slice
sliceTest = '0123456789'
print(sliceTest[:], sliceTest[0:], sliceTest[5:], sliceTest[3:6], sliceTest[-3:], sliceTest[8:-3], sliceTest[-6:-2])
print(sliceTest[::2], sliceTest[-1::-1], sliceTest[::-1])

# split
splitTest = 'hello one, hello two,\nhello three,\thello four'
print(splitTest.split(','))
print(splitTest.split())

# join
joinTest = ['hello one', 'hello two', 'hello three']
print(', '.join(joinTest))
print('\n'.join(joinTest))

# string functions

stringFuncTest = 'hello one, Hello two, hello Three...'
print(stringFuncTest.startswith('hello'))
print(stringFuncTest.endswith('Three...'))
print(stringFuncTest.find('one'))
print(stringFuncTest.rfind('two'))
print(stringFuncTest.count('hello'))
print(stringFuncTest.isalnum())
print(stringFuncTest.isalpha())
print(stringFuncTest.strip('.'))
print(stringFuncTest.capitalize())
print(stringFuncTest.title())
print(stringFuncTest.upper())
print(stringFuncTest.lower())
print(stringFuncTest.swapcase())
print(stringFuncTest.center(100))
print(stringFuncTest.ljust(50))
print(stringFuncTest.rjust(50))

print(stringFuncTest.replace('hello', 'replaced'))
print(stringFuncTest.replace('hello', 'replaced', 1))
