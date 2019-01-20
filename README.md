# R-Cubed
Using QR Codes to Determine Recycle-ability, A Proof of Concept

# H.A.R.D. Hack
This project was started and finished during [H.A.R.D Hack 2019 in UCSD](http://hardhacksd.com). Thanks to the mentors, sponsors, and event holders for making this event possible.

# The Premise
We wanted to help improve recycling as easily as possible. We decided on placing a small QR code on a given packaging to allow an automated sorting system sort "garbage" into more effective "recycling" and "trash" piles, reducing the amount of items that goes to a landfill. We expect that these QR codes will be placed on packaging by the manufacturer.

# How It Works
By using the [Webcam Capture API](https://github.com/sarxos/webcam-capture) and the [ZXing QR/Barcode Decoder](https://github.com/zxing/zxing), we are able to use a webcam to scan for QR/Barcodes and read their value. If it matches a given value, we will display that it can be recycled. Otherwise, it will display that it cannot be recycled. The system would be able to reroute that object accordingly.

# Possible Future Improvements
The addition of more values for the QR code, such as the type of plastic and if it can be composted, would be more effective and more beneficial to the environment. More possible ideas are as follow:
- More cameras to more accurately read QR codes
- The use of more high resolution cameras to easily identify and decode QR codes
- Implementing mashine learning to better identify objects without the need for a QR code

# License
This program is licensed under the Apache License Version 2.0. Please view the [LICENSE](/LICENSE) file for more information.
