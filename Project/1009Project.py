import PySimpleGUI as gui
import sqlite3
import matplotlib.pyplot as plt
import os


conn = sqlite3.connect('crawler.db')
sql = conn.cursor()

sql.execute('''CREATE TABLE IF NOT EXISTS stockdb
           ([ID] INTEGER PRIMARY KEY,[Stock] text, [Source] text, [Date_created] date, [Comment] text,[Sentiment] text)''')
sql.execute('''DELETE FROM stockdb''')

conn.commit()
#sql.execute('''Insert INTO stockdb(Stock,Source) VALUES("haha","lol")''')
sql.execute('''SELECT * FROM stockdb ''')
print(sql.fetchall())


gui.change_look_and_feel('BlueMono')
sourceSelection = ("GME", "DOGE", "AMC")
layout = [
    [gui.Text("Stocks")],
    [gui.Combo(sourceSelection, size=(40, 7), enable_events=True, key='-COMBO-')],
    [gui.Text("Source")],
    [gui.Checkbox('All', change_submits= True, default=False, key='-CheckAll-'),
     gui.Checkbox('Reddit', key=1),
     gui.Checkbox('Twitter', key=2),
     gui.Checkbox('StockTwits', key=3)],
    [gui.Button("GO")]
]

# Create the window
window = gui.Window("Data Crawler Application", layout, margins=(300, 250))





# Create an event loop
while True:
    socialMedia = []
    event, values = window.read()
    #Check all boxes when selected

    if event == '-CheckAll-':
        if values['-CheckAll-'] is True:
            window.find_element('-CheckAll-').Update(text='Deselect', value=True)
            for x in range(1,4):
                window.Element(x).Update(True)
        #Uncheck all boxes
        else:
            window.find_element('-CheckAll-').Update(text='All', value=False)
            for x in range(1, 4):
                window.Element(x).Update(False)



    #Once everything is selected, time to start crawling
    if event == "GO":
        #If check all is select, the first item in the array will store it
        if values['-CheckAll-'] is True:
            socialMedia.append('all')
        #Else assign the key of the social media into the array
        else:
            for x in range(1,4):
                print(window.find_element(x))
                if values[x] is True:
                    socialMedia.append(x)

        #Check for which stocks is selected
        if values['-COMBO-'] != '':
            combo = values['-COMBO-']  # use the combo key
            break

    if event == gui.WIN_CLOSED:
        print("Deleting...")
        sql.execute('''DELETE FROM stockdb''')
        conn.close()
        quit()


window.close()

print(combo) #Which stock is chosen
print(socialMedia) #key of social media

if socialMedia == 1:
    os.system("java -classpath C:/Users/caizh/Desktop/reddit.jar  MainPage "+combo) #(java -classpath -location- -mainclass-)
elif socialMedia == 2:
    os.system("java -classpath C:/Users/caizh/Desktop/Twitter.jar  TwitterCrawler "+combo) #(java -classpath -location- -mainclass-)

#layout = [
#                [gui.Text("This is the 2nd layout")],
#               [gui.Text("Data should be shown here")],
#                [gui.Button("Close")]]
######GUI
labels = 'Frogs', 'Hogs', 'Dogs', 'Logs'
sizes = [15, 30, 45, 10]
explode = (0, 0.1, 0, 0)  # only "explode" the 2nd slice (i.e. 'Hogs')

fig1, ax1 = plt.subplots()
ax1.pie(sizes, explode=explode, labels=labels, autopct='%1.1f%%',
        shadow=True, startangle=90)
ax1.axis('equal')  # Equal aspect ratio ensures that pie is drawn as a circle.
plt.savefig('books_read.png')
######GUI
tab1_layout = [[gui.Text('Tab 1')],
               [gui.Text('Put your layout in here')],
               [gui.Text('Input something')],[gui.Image(r'books_read.png')]]

tab2_layout = [[gui.Text('Tab 2')]]
tab3_layout = [[gui.Text('Tab 3')]]
tab4_layout = [[gui.Text('Tab 3')]]

# The TabgGroup layout - it must contain only Tabs
tab_group_layout = [[gui.Tab('Tab 1', tab1_layout, font='Courier 15', key='-TAB1-'),
                     gui.Tab('Tab 2', tab2_layout, key='-TAB2-'),
                     gui.Tab('Tab 3', tab3_layout, key='-TAB3-'),
                     gui.Tab('Tab 4', tab4_layout, key='-TAB4-'),
                     ]]

# The window layout - defines the entire window
layout = [[gui.TabGroup(tab_group_layout,
                       enable_events=True,
                       key='-TABGROUP-')],
          [gui.Text('Make tab number'), gui.Input(key='-IN-', size=(3,1)), gui.Button('Invisible'),
           gui.Button('Visible'), gui.Button('Select')]]

secondWin = gui.Window('Data Crawler Application', layout)

while True:
        event, values = secondWin.read()
        if event == gui.WIN_CLOSED:
            print("Deleting...")
            sql.execute('''DELETE FROM stockdb''')
            conn.close()
            os.remove("books_read.png")
            exit()

secondWin.close()



