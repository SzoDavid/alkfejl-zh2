
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
original_image = cv2.imread('01_shapes_input.jpg', cv2.IMREAD_COLOR)


# 2.) A színes képen hajtson végre Gauss simítást
# 5x5-ös méretben 4.0 szórással!
# 1 pont
blurred_gauss_image = cv2.GaussianBlur(original_image, (5, 5), sigmaX=4.0, sigmaY=4.0)


# Jelenítse meg az eredményt!
cv2.imshow('Blurred Gauss Image', blurred_gauss_image)


# 3.) A színcsatornák vizsgálatával szegmentálja a zöld színű területeket!
# Kisebb szegmentálási hibák maradhatnak a képen.
# 3 pont
gray_image = cv2.cvtColor(blurred_gauss_image, cv2.COLOR_BGR2GRAY)
threshold_image = cv2.adaptiveThreshold(gray_image, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY, 11, 40)
green_mask = cv2.inRange(blurred_gauss_image, (0, 50, 0), (60, 255, 60))
segmented_image = cv2.bitwise_and(threshold_image, green_mask)

# Jelenítse meg az eredményt!
cv2.imshow('Segmented Image', segmented_image)


# 4.) Morfológiai műveletekkel javítson a szegmentálási eredményen!
# Két eróziót és két dilatációt alkalmazzon!
# Válasszon megfelelő struktúráló elemet!
# 3 pont
struct = cv2.getStructuringElement(cv2.MORPH_RECT, (5, 5))
segmented_morph_image = segmented_image.copy()
segmented_morph_image = cv2.dilate(segmented_morph_image, struct)
segmented_morph_image = cv2.erode(segmented_morph_image, struct)
segmented_morph_image = cv2.erode(segmented_morph_image, struct)
segmented_morph_image = cv2.dilate(segmented_morph_image, struct)


# Jelenítse meg az eredményt!
cv2.imshow('Segmented Morph Image', segmented_morph_image)


# 5.) Keressem kontúrokat a szegmentált képen!
# Figyeljen arra, hogy milyen módot használ, hogy alkalmas legyen külső-belső
# kontúr vizsgálatra!
# 1 pont
contours, hierarchy = cv2.findContours(segmented_morph_image, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)
img = cv2.cvtColor(segmented_morph_image, cv2.COLOR_GRAY2BGR)


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
    cv2.drawContours(original_image, draw_contours, -1, (0, 255, 0), 3, cv2.LINE_4)

# Jelenítse meg az eredményt!
cv2.imshow('Original Image', original_image)

# Írja ki az eredmény képet JPG formátumban fájlba!
# 0,5 pont
cv2.imwrite('result/output.jpg', original_image)


cv2.waitKey(0)

cv2.destroyAllWindows()
