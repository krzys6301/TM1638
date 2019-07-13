package kcichocki.tm1638;

import java.util.HashMap;

// The bits are displayed by mapping bellow
//  -- 0 --
// |       |
// 5       1
// |       |
//  -- 6 --
// |       |
// 4       2
// |       |
//  -- 3 --   o 7
public class Font {
	HashMap<Character, Byte> font = new HashMap<>();

	{
		font.put(' ', (byte) 0b00000000); // (32) <space>
		font.put('!', (byte) 0b10000110); // (33) !
		font.put('"', (byte) 0b00100010); // (34) "
		font.put('(', (byte) 0b00110000); // (40) (
		font.put(')', (byte) 0b00000110); // (41) )
		font.put(',', (byte) 0b00000100); // (44) ,
		font.put('-', (byte) 0b01000000); // (45) -
		font.put('.', (byte) 0b10000000); // (46) .
		font.put('/', (byte) 0b01010010); // (47) /
		font.put('0', (byte) 0b00111111); // (48) 0
		font.put('1', (byte) 0b00000110); // (49) 1
		font.put('2', (byte) 0b01011011); // (50) 2
		font.put('3', (byte) 0b01001111); // (51) 3
		font.put('4', (byte) 0b01100110); // (52) 4
		font.put('5', (byte) 0b01101101); // (53) 5
		font.put('6', (byte) 0b01111101); // (54) 6
		font.put('7', (byte) 0b00100111); // (55) 7
		font.put('8', (byte) 0b01111111); // (56) 8
		font.put('9', (byte) 0b01101111); // (57) 9
		font.put('=', (byte) 0b01001000); // (61) =
		font.put('?', (byte) 0b01010011); // (63) ?
		font.put('@', (byte) 0b01011111); // (64) @
		font.put('A', (byte) 0b01110111); // (65) A
		font.put('B', (byte) 0b01111111); // (66) B
		font.put('C', (byte) 0b00111001); // (67) C
		font.put('D', (byte) 0b00111111); // (68) D
		font.put('E', (byte) 0b01111001); // (69) E
		font.put('F', (byte) 0b01110001); // (70) F
		font.put('G', (byte) 0b00111101); // (71) G
		font.put('H', (byte) 0b01110110); // (72) H
		font.put('I', (byte) 0b00000110); // (73) I
		font.put('J', (byte) 0b00011111); // (74) J
		font.put('K', (byte) 0b01101001); // (75) K
		font.put('L', (byte) 0b00111000); // (76) L
		font.put('M', (byte) 0b00010101); // (77) M
		font.put('N', (byte) 0b00110111); // (78) N
		font.put('O', (byte) 0b00111111); // (79) O
		font.put('P', (byte) 0b01110011); // (80) P
		font.put('Q', (byte) 0b01100111); // (81) Q
		font.put('R', (byte) 0b00110001); // (82) R
		font.put('S', (byte) 0b01101101); // (83) S
		font.put('T', (byte) 0b01111000); // (84) T
		font.put('U', (byte) 0b00111110); // (85) U
		font.put('V', (byte) 0b00101010); // (86) V
		font.put('W', (byte) 0b00011101); // (87) W
		font.put('X', (byte) 0b01110110); // (88) X
		font.put('Y', (byte) 0b01101110); // (89) Y
		font.put('Z', (byte) 0b01011011); // (90) Z
		font.put('[', (byte) 0b00111001); // (91) [
		font.put(']', (byte) 0b00001111); // (93) ]
		font.put('_', (byte) 0b00001000); // (95) _
		font.put('`', (byte) 0b00100000); // (96) `
		font.put('a', (byte) 0b01011111); // (97) a
		font.put('b', (byte) 0b01111100); // (98) b
		font.put('c', (byte) 0b01011000); // (99) c
		font.put('d', (byte) 0b01011110); // (100) d
		font.put('e', (byte) 0b01111011); // (101) e
		font.put('f', (byte) 0b00110001); // (102) f
		font.put('g', (byte) 0b01101111); // (103) g
		font.put('h', (byte) 0b01110100); // (104) h
		font.put('i', (byte) 0b00000100); // (105) i
		font.put('j', (byte) 0b00001110); // (106) j
		font.put('k', (byte) 0b01110101); // (107) k
		font.put('l', (byte) 0b00110000); // (108) l
		font.put('m', (byte) 0b01010101); // (109) m
		font.put('n', (byte) 0b01010100); // (110) n
		font.put('o', (byte) 0b01011100); // (111) o
		font.put('p', (byte) 0b01110011); // (112) p
		font.put('q', (byte) 0b01100111); // (113) q
		font.put('r', (byte) 0b01010000); // (114) r
		font.put('s', (byte) 0b01101101); // (115) s
		font.put('t', (byte) 0b01111000); // (116) t
		font.put('u', (byte) 0b00011100); // (117) u
		font.put('v', (byte) 0b00101010); // (118) v
		font.put('w', (byte) 0b00011101); // (119) w
		font.put('x', (byte) 0b01110110); // (120) x
		font.put('y', (byte) 0b01101110); // (121) y
		font.put('z', (byte) 0b01000111); // (122) z
		font.put('{', (byte) 0b01000110); // (123) {
		font.put('|', (byte) 0b00000110); // (124) |
		font.put('}', (byte) 0b01110000); // (125) }
		font.put('~', (byte) 0b00000001); // (126) ~
	}

	public byte translateChar(char c) {
		return font.get(c);
	}
}
