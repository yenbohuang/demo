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