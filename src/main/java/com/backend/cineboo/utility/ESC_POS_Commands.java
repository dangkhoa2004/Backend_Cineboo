package com.backend.cineboo.utility;

public class ESC_POS_Commands {
    // Initialize printer
    final static byte[] initializePrinter = new byte[]{0x1B, 0x40}; // ESC @

    // New line (line feed)
    final static byte[] newLine = new byte[]{0x0A}; // LF

    // Bold text ON
    final static   byte[] boldOn = new byte[]{0x1B, 0x45, 0x01}; // ESC E 1

    // Bold text OFF
    final static  byte[] boldOff = new byte[]{0x1B, 0x45, 0x00}; // ESC E 0

    // Underline ON
    final static byte[] underlineOn = new byte[]{0x1B, 0x2D, 0x01}; // ESC - 1

    // Underline OFF
    final static   byte[] underlineOff = new byte[]{0x1B, 0x2D, 0x00}; // ESC - 0

    // Double height and width ON
    final static byte[] doubleHeightWidthOn = new byte[]{0x1D, 0x21, 0x11}; // GS ! 17

    // Double height and width OFF
    final static byte[] doubleHeightWidthOff = new byte[]{0x1D, 0x21, 0x00}; // GS ! 0

    // Align text to left
    final static  byte[] alignLeft = new byte[]{0x1B, 0x61, 0x00}; // ESC a 0

    // Align text to center
    final static  byte[] alignCenter = new byte[]{0x1B, 0x61, 0x01}; // ESC a 1

    // Align text to right
    final static byte[] alignRight = new byte[]{0x1B, 0x61, 0x02}; // ESC a 2

    // Cut paper (partial cut)
    final static byte[] cutPaperPartial = new byte[]{0x1D, 0x56, 0x01}; // GS V 1

    // Cut paper (full cut)
    final  static    byte[] cutPaperFull = new byte[]{0x1D, 0x56, 0x00}; // GS V 0



    // Beep sound
    final static byte[] beep = new byte[]{0x1B, 0x42, 0x02, 0x03}; // ESC B n t (n = beep count, t = duration)

    // Set character size
// Replace `n` with size (e.g., 0x00 for normal, 0x11 for double width/height)
    final static  byte[] setCharacterSize = new byte[]{0x1D, 0x21, 0x00}; // GS ! n

    // Reverse text ON
    final static  byte[] reverseOn = new byte[]{0x1D, 0x42, 0x01}; // GS B 1

    // Reverse text OFF
    final static byte[] reverseOff = new byte[]{0x1D, 0x42, 0x00}; // GS B 0

    // Print barcode (Code 39 example)
// Replace `n` with the barcode data length
    final static  byte[] printBarcode = new byte[]{0x1D, 0x6B, 0x04}; // GS k 4 <data>

}
