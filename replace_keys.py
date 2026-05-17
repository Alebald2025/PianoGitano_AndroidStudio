with open('app/src/main/res/layout/activity_main.xml', 'r', encoding='utf-8') as f:
    content = f.read()

# Reemplaza en una sola linea
content = content.replace(
    '<View android:id="@+id/key_',
    '<ImageButton android:padding="0dp" android:scaleType="fitXY" android:id="@+id/key_'
)

# Reemplaza en multiples lineas
content = content.replace(
    '<View\n                android:id="@+id/key_',
    '<ImageButton android:padding="0dp" android:scaleType="fitXY"\n                android:id="@+id/key_'
)

with open('app/src/main/res/layout/activity_main.xml', 'w', encoding='utf-8') as f:
    f.write(content)

print("Updated correctly!")
