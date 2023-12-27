
compile the code and run the code using makefile : 

make

OR

gcc huffman_code.c
./a.out


(Note1 : Add the input string to test in input.txt file, i.e use the file with same name)
(Note2 : Redirect the output to an output file when input file is large
make > result.txt)

Example:
sshah13@remote05:~/PST/Group14$ make
rm -f huffman_code
gcc -o huffman_code huffman_code.c
./huffman_code 
INPUT STRING: 
aaaaabbbbbccccccccccccddddd
FREQUENCY COUNT OF CHARACTERS
(a): 5
(b): 5
(c): 12
(d): 5
PRINT HUFFMAN CODES
(c): 0
(d): 10
(a): 110
(b): 111
ENCODED STRING: 
�m��*�
DECODED STRING: 
aaaaabbbbbccccccccccccddddd
