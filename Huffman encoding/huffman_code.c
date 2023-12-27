// Sakshi Shah

#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include <limits.h>

#define SYMBOL_COUNT 256
#define MAX_TREE_NODES 2 * SYMBOL_COUNT - 1 // Maximum number of nodes in a huffman tree : 2n-1
#define MAX_CODE_LENGTH 32 // Maximum length of a huffman code
#define BITS_IN_BYTE 8

// Struct to represent a node in a huffman tree
typedef struct huffman_node {
    int freq;               // Frequency of character in the message
    char character;          // Character
    int left_child;         // Index of left child node in the tree array
    int right_child;        // Index of right child node in the tree array
    int parent;            // Index of Parent node in the tre array
} huffman_node;

// Struct to store symbol and its count from the input string
typedef struct symbol_count{
    char symbol;        
    unsigned int count;
} symbol_count;

// Struct to store huffman encoding code
typedef struct huffman_code{
    char symbol;
    int huff_code[SYMBOL_COUNT];
    int code_length;
} huffman_code;

int freq_table[SYMBOL_COUNT] = {0};
huffman_node tree[SYMBOL_COUNT * 2 - 1] = {0};
int leaf_node_count = 0;
int input_msg_length = 0;
int nodes_remaining = SYMBOL_COUNT;
symbol_count counts[SYMBOL_COUNT];
struct huffman_code huff_codes[SYMBOL_COUNT] = {0};
int encoded_string_length = 0;
int char_huff_code_length[99999999];

// Function to read input file and generate character frequency table
void count_characters() {
    FILE* fp = fopen("input.txt", "r");
    if (fp == NULL) {
        printf("Error: could not open input file.\n");
        exit(1);
    }

    char c;
    while ((c = fgetc(fp)) != EOF) {
        freq_table[(int)c]++;
        input_msg_length++;
        printf("%c", c);
    }
    fclose(fp);
}

// Helper function to find the index of the node with minimum frequency
int findMinFreqNode(huffman_node* nodes, int n, int exclude) {
    int minFreq = INT_MAX;
    int minIndex = -1;
    for (int i = 0; i < n; i++) {
        if (i == exclude || nodes[i].parent != -1) {
            continue;
        }
        if (nodes[i].freq < minFreq) {
            minFreq = nodes[i].freq;
            minIndex = i;
        }
    }
    return minIndex;
}

// Build the Huffman tree
void buildHuffmanTree() {
    int n = leaf_node_count * 2 - 1;
    for (int i = leaf_node_count; i < n; i++) {
        int min1 = findMinFreqNode(tree, n, -1);
        int min2 = findMinFreqNode(tree, n, min1);
        tree[min1].parent = i;
        tree[min2].parent = i;
        tree[i].freq = tree[min1].freq + tree[min2].freq;
        tree[i].left_child = min1;
        tree[i].right_child = min2;
        tree[i].parent = -1;
        tree[i].character = '*';
    }
}

void generate_huffman_codes_helper(int node, int* code, int code_length)
{
    if (tree[node].left_child == -1 && tree[node].right_child == -1)
    {
        // store the Huffman code for the leaf node
        huff_codes[node].symbol = tree[node].character;
        huff_codes[node].code_length = code_length;
        printf("(%c): ", tree[node].character);
        for (int i = 0; i < code_length; i++)
        {
            encoded_string_length+=freq_table[(int)tree[node].character];
            printf("%d", code[i]);
            huff_codes[node].huff_code[i] = code[i];
        }
        printf("\n");
        return;
    }
    // traverse left child
    code[code_length] = 0;
    generate_huffman_codes_helper(tree[node].left_child, code, code_length+1);
    // traverse right child
    code[code_length] = 1;
    generate_huffman_codes_helper(tree[node].right_child, code, code_length+1);
}

void generate_huffman_codes()
{
    int code[SYMBOL_COUNT];
    generate_huffman_codes_helper(2*leaf_node_count-2, code, 0);
}


void store_encode_result() {
    char ch;
    FILE *output_fp, *input_fp;
    input_fp = fopen("input.txt", "r");
    output_fp = fopen("output.txt", "w");
    unsigned char buffer = 0;
    int bits_remaining = 8;
    int count = 0;
    int code_length_cnt = 0;
    while ((ch = fgetc(input_fp)) != EOF) {
        for(int i = 0; i < leaf_node_count; i++) {
            if(ch == huff_codes[i].symbol) {
                char_huff_code_length[code_length_cnt++] = huff_codes[i].code_length;
                for(int j = 0; j < huff_codes[i].code_length; j++) {
                    buffer <<= 1;
                    buffer |= huff_codes[i].huff_code[j];
                    bits_remaining--;
                    if(bits_remaining == 0) {
                        printf("%c", buffer);
                        fwrite(&buffer, sizeof(unsigned char), 1, output_fp);
                        bits_remaining = 8;
                        buffer = 0;
                        
                    }
                }
            }
        }
    }
    if(bits_remaining != 8) {
        buffer <<= bits_remaining;
        printf("%c", buffer);
        fwrite(&buffer, sizeof(unsigned char), 1, output_fp);
    }
    fclose(input_fp);
    fclose(output_fp);
}


void decode()
{
    FILE *input_fp, *output_fp;
    input_fp = fopen("output.txt", "rb");
    if(input_fp == NULL)
    {
        printf("File not found");
        exit(0);
    }
    output_fp = fopen("decoded.txt", "w");
    unsigned char buffer = 0;
    int encodedBitCount = 0, i = 0, count = 0, node = 2 * leaf_node_count - 2;
    int codeLength = char_huff_code_length[0];
    while(fread(&buffer, sizeof(buffer), 1, input_fp) == 1) {

        for(int charBit = BITS_IN_BYTE - 1; charBit >= 0; charBit--) {
            if(count >= encoded_string_length) break;

            int bit = (buffer & (1 << charBit)) != 0 ? 1 : 0;
            node = (bit == 1) ? tree[node].right_child : tree[node].left_child;

            if(tree[node].left_child == -1 && tree[node].right_child == -1) {
                fprintf(output_fp, "%c", tree[node].character);
                printf("%c", tree[node].character);
                node = 2 * leaf_node_count - 2;
            }
            encodedBitCount++;
            if(encodedBitCount == codeLength) {
                i++;
                if(i < leaf_node_count) {
                    codeLength = char_huff_code_length[i];
                    node = 2 * leaf_node_count - 2;
                }
                encodedBitCount = 0;
            }
            count++;
        }
        if(count >= encoded_string_length) break;
    }
    printf("\n");
    fclose(output_fp);
    fclose(input_fp);
}

int main()
{
    printf("INPUT STRING: \n");
    count_characters();
    printf("\n");

    for (int i = 0; i < SYMBOL_COUNT; i++) {
        if(freq_table[i]>0)
            leaf_node_count++;
    }

    int index =0;
    for (int i = 0; i < SYMBOL_COUNT; i++) {
        if(freq_table[(int)i]>0)
        {
            counts[index].symbol = i;
            counts[index].count = freq_table[(int)i];  
            //printf("%c\n", counts[i].symbol) ;
            index++;   
        }  
    }

    printf("FREQUENCY COUNT OF CHARACTERS\n");
    for (int i = 0; i < leaf_node_count; i++) {
        printf("(%c): %d\n", counts[i].symbol, freq_table[(int)counts[i].symbol]) ;
    }

    //printf("Tree %d\n", leaf_node_count);
    for(int i = 0; i < leaf_node_count; i++)
    {
        tree[i].character = counts[i].symbol;
        tree[i].freq = freq_table[(int)tree[i].character];
        tree[i].left_child = -1;
        tree[i].right_child = -1;
        tree[i].parent = -1;
        //printf("freq %c %d \n", counts[i].symbol, freq_table[(int)tree[i].character]);

    }

    buildHuffmanTree();

    printf("PRINT HUFFMAN CODES\n");
    generate_huffman_codes();

    printf("ENCODED STRING: \n");
    store_encode_result();
    printf("\n");

    printf("DECODED STRING: \n");
    decode();
}
