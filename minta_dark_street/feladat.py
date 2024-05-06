# Név:
# Neptun kód:
import cv2
import numpy as np

# A részeredmények a következő állományokban találhatóak
#
# 01_input.jpg
# 03_L_histeq.png
# 04_L_histstretch.png
# 06_mod_bgr.png
# 07_cut.png


# Részeredmények megjelenítése a programban külön ablakokban.
# Összesen 1 pont

# 1.) Olvassa be a 01_input.jpg kontrasztszegény színes képet!
#     1 pont

original_img = cv2.imread('01_input.jpg', cv2.IMREAD_COLOR)
cv2.imshow('original', original_img)
cv2.waitKey(0)
cv2.destroyAllWindows()

# 2.) Konvertálja át BGR-ből Lab színtérbe és bontsa fel csatornákra!
#     2 pont

lab_img = cv2.cvtColor(original_img, cv2.COLOR_BGR2LAB)
ch_l, ch_a, ch_b = cv2.split(lab_img)

# Jelenítse meg a részfeladat eredményét!
cv2.imshow('lab_img', lab_img)
cv2.waitKey(0)
cv2.destroyAllWindows()

# 3.) A szürkeárnyalatos (L) csatornán hajtson végre hisztogram kiegyenlítést!
#     1 pont
img_histeq = cv2.equalizeHist(ch_l)

# Jelenítse meg a részfeladat eredményét!

cv2.imshow('hist', img_histeq)
cv2.waitKey(0)
cv2.destroyAllWindows()


# 4.) A hisztogram kiegyenlített képen hajtson végre kontraszt széthúzást egy
#     megfelelő tartomány választásával, hogy az égbolt egyenletes fekete
#     színű legyen.
#     3 pont
th_lower = 170
th_upper = 255
img_histeq[img_histeq > th_upper] = th_upper
img_histeq[img_histeq < th_lower] = th_lower
hist_stretch = cv2.normalize(img_histeq, None, 0, 255, norm_type=cv2.NORM_MINMAX, dtype=cv2.CV_8UC1)

# cv2.imshow('hist_stretch', hist_stretch)

# 5.) Az a és b csatornák intenzitásait szorozza meg 1.2 értékkel!
#     Ügyeljen a túlcsordulások megfelelő csonkolásos kezelésére!
#     2 pont

ch_a = np.clip(ch_a*1.2, 0, 255).astype('uint8')
ch_b = np.clip(ch_a*1.2, 0, 255).astype('uint8')

# Jelenítse meg a részfeladat eredményét!

cv2.imshow('ch_a', ch_a)
cv2.imshow('ch_b', ch_b)
cv2.waitKey(0)
cv2.destroyAllWindows()


# 6.) A módosított L, a és b csatornákat fűzze össze egy 3-csatornás képpé,
#     és alakítsa vissza BGR színtérbe!
#     2 pont
merged = cv2.merge([hist_stretch, ch_a, ch_b])

newMerged = cv2.cvtColor(merged, cv2.COLOR_LAB2BGR)

# Jelenítse meg a részfeladat eredményét!

cv2.imshow('merged', newMerged)
cv2.waitKey(0)
cv2.destroyAllWindows()

# 7.) A képből vágja ki a házat tartalmazó részt!
#     2 pont

x1, y1 = 750, 100
x2, y2 = 947, 449

house = merged[y1:y2, x1:x2]

# Jelenítse meg a részfeladat eredményét!

cv2.imshow('house', house)
cv2.waitKey(0)
cv2.destroyAllWindows()

# 8.) A kivágott részen hajtson végre Gauss-simítást, és az eredményt
#     másolja vissza az eredeti képbe!
#     2 pont

gauss = cv2.GaussianBlur(house, (5, 5), sigmaX=2.0, sigmaY=2.0)

newMerged[y1:y2, x1:x2] = gauss

# 9.) A végeredményt írja ki egy png típusú állományba!
#     0,5 pont

cv2.imshow('merged', newMerged)

cv2.waitKey(0)
cv2.destroyAllWindows()
