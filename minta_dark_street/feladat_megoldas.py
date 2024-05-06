"""dark street feladat"""
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

img = cv2.imread("01_input.jpg")

# 2.) Konvertálja át BGR-ből Lab színtérbe és bontsa fel csatornákra!
#     2 pont

lab_img = cv2.cvtColor(img, cv2.COLOR_BGR2LAB)
chan_l, chan_a, chan_b = cv2.split(lab_img)

# Jelenítse meg a részfeladat eredményét!

cv2.imshow("bgr2lab", lab_img)
cv2.waitKey(0)
cv2.destroyWindow("bgr2lab")

# 3.) A szürkeárnyalatos (L) csatornán hajtson végre hisztogram kiegyenlítést!
#     1 pont

hist_eq = cv2.equalizeHist(chan_l)

# Jelenítse meg a részfeladat eredményét!

cv2.imshow("hist_eq", hist_eq)
cv2.waitKey(0)
cv2.destroyWindow("hist_eq")


# 4.) A hisztogram kiegyenlített képen hajtson végre kontraszt széthúzást egy
#     megfelelő tartomány választásával, hogy az égbolt egyenletes fekete
#     színű legyen.
#     3 pont

th_lower = 170
th_upper = 255
hist_eq[hist_eq > th_upper] = th_upper
hist_eq[hist_eq < th_lower] = th_lower

hist_stretch = cv2.normalize(hist_eq, None, 0, 255, norm_type=cv2.NORM_MINMAX, dtype=cv2.CV_8UC1)

# cv2.imshow("thresh_stretch", hist_stretch)
# cv2.waitKey(0)
# cv2.destroyWindow("thresh_stretch")


# 5.) Az a és b csatornák intenzitásait szorozza meg 1.2 értékkel!
#     Ügyeljen a túlcsordulások megfelelő csonkolásos kezelésére!
#     2 pont

chan_a = np.clip(chan_a*1.2, 0, 255).astype('uint8')
chan_b = np.clip(chan_b*1.2, 0, 255).astype('uint8')

# Jelenítse meg a részfeladat eredményét!

cv2.imshow("chan_a_intensity", chan_a)
cv2.imshow("chan_b_intensity", chan_b)
cv2.waitKey(0)
cv2.destroyWindow("chan_a_intensity")
cv2.destroyWindow("chan_b_intensity")

# 6.) A módosított L, a és b csatornákat fűzze össze egy 3-csatornás képpé,
#     és alakítsa vissza BGR színtérbe!
#     2 pont

merged = cv2.merge([hist_stretch, chan_a, chan_b])
merged = cv2.cvtColor(merged, cv2.COLOR_LAB2BGR)

# Jelenítse meg a részfeladat eredményét!

cv2.imshow("merged", merged)
cv2.waitKey(0)
cv2.destroyWindow("merged")

# 7.) A képből vágja ki a házat tartalmazó részt!
#     2 pont

x1, y1 = 750, 100  # bal felso sarka a kivagando kepnek
x2, y2 = 947, 429  # jobb also sarka a kivagando kepnek
# amikor imshownal latod a kepet es hoverelsz rajta kurzorral latszanak a koordinatak a bal also sarokban
# vagy csak az eredeti es a kivagott mintakep dimenzioibol visszaszamolsz idk

house = merged[y1:y2, x1:x2]

# Jelenítse meg a részfeladat eredményét!

cv2.imshow("haziko", house)
cv2.waitKey(0)
cv2.destroyWindow("haziko")

# 8.) A kivágott részen hajtson végre Gauss-simítást, és az eredményt
#     másolja vissza az eredeti képbe!
#     2 pont

dr_ghauss = cv2.GaussianBlur(house, (5, 5), sigmaX=2.0, sigmaY=2.0)

# !!============== nem mukodik ==============!!

# roi = gauss[y1:y2, x1:x2]
# res = cv2.add(roi, gauss)
# gauss[y1:y2, x1:x2] = res
#
# cv2.imshow("res", res)
# cv2.waitKey(0)
# cv2.destroyWindow("res")

res = merged.copy()  # elsimitott kepet nem tudtam rarakni az eredetire szoval az kimarad boo hoo

# 9.) A végeredményt írja ki egy png típusú állományba!
#     0,5 pont

cv2.imwrite("res.png", res)
