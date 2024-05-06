# Név:
# Neptun kód: 
# Gép sorszáma: 

import cv2
import numpy as np

# Elérhető részeredmény képek:
# 02_shapes_blur.jpg
# 03_shapes_segmented.png
# 04_shapes_morph.png
# 07_shapes_result.jpg

# Részeredmények megjelenítése a programban külön ablakokban.
# Összesen 1 pont


# Olvassa be az 01_shapes_input.jpg színes képet!
# Megjelenítenie nem kell!
# 0,5 pont
img = cv2.imread('01_shapes_input.jpg', cv2.IMREAD_COLOR)
cv2.imshow('img', img)

# 2.) A színes képen hajtson végre Gauss simítást
# 5x5-ös méretben 4.0 szórással!
# 1 pont

gaussimg = cv2.GaussianBlur(img, (5, 5), sigmaX=4, sigmaY=4)

# Jelenítse meg az eredményt!

cv2.imshow('gaussimg', gaussimg)


# 3.) A színcsatornák vizsgálatával szegmentálja a zöld színű területeket!
# Kisebb szegmentálási hibák maradhatnak a képen.
# 3 pont

def hsv_segment(interval_H, interval_S, interval_V, wndtitle):
    global imgHSV

    minHSV = np.array([interval_H[0], interval_S[0], interval_V[0]])
    maxHSV = np.array([interval_H[1], interval_S[1], interval_V[1]])
    segmented = cv2.inRange(imgHSV, minHSV, maxHSV)
    return segmented


imgHSV = cv2.cvtColor(gaussimg, cv2.COLOR_BGR2HSV)
global segmented

# Narancsok
segmented = hsv_segment((60, 70), (90, 190), (50, 150), 'Zold')

# Jelenítse meg az eredményt!

cv2.imshow('segmented', segmented)

# 4.) Morfológiai műveletekkel javítson a szegmentálási eredményen!
# Két eróziót és két dilatációt alkalmazzon!
# Válasszon megfelelő struktúráló elemet!
# 3 pont

struct = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (5,5))
segmented = cv2.dilate(segmented, struct)
segmented = cv2.erode(segmented, struct)
segmented = cv2.dilate(segmented, struct)
segmented = cv2.erode(segmented, struct)


cv2.imshow('morphed', segmented)

# Jelenítse meg az eredményt!


# 5.) Keressem kontúrokat a szegmentált képen!
# Figyeljen arra, hogy milyen módot használ, hogy alkalmas legyen külső-belső
# kontúr vizsgálatra!
# 1 pont


contours, hierarchy = cv2.findContours(segmented, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)


# 6.) Ellenőrizze a kontúrokat, és csak az alábbiakat tartsa meg:
# - A kontúr külső kontúr és tartalmaz üreget.
# - A kontúr cirkularitási jellemzője alapján elegendően kör alakú.
# 4 pont
draw_contours = []
for cntrIdx in range(0, len(contours)):
    area = cv2.contourArea(contours[cntrIdx])
    perimeter = cv2.arcLength(contours[cntrIdx], True)

    circularity = 4 * np.pi * area / (perimeter ** 2)

    if circularity > 0.8 and hierarchy[0][cntrIdx][2] != -1:
        draw_contours.append(contours[cntrIdx])



# 7.) Rajzolja be az előző lépés kritériumat teljesítő kontúrok körvonalát
# világoszöld színnel az eredeti színes képmátrixba, 3 képpont szélességben.
# 2 pont
for contour in draw_contours:
    cv2.drawContours(img, draw_contours, -1, (0, 255, 0), 3, cv2.LINE_4)


# Jelenítse meg az eredményt!

cv2.imshow('Original Image', img)


# Írja ki az eredmény képet JPG formátumban fájlba!
# 0,5 pont


cv2.waitKey(0)
cv2.destroyAllWindows()
