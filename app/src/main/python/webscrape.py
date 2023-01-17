import time
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager

def hi(a):
    print("helloworld")
    return a

def arasBot(a):
    options = selenium.webdriver.ChromeOptions()
    driver = webdriver.Chrome(ChromeDriverManager().install(), options=options)
    url = "https://www.araskargo.com.tr/tr/"
    driver.get(url)

    driver.maximize_window()


    cargo_num = driver.find_element("name",'InputCargo1')
    cargo_num.send_keys(a)

    oturum_ac  = driver.find_element("xpath",'//*[@id="ButtonCargoTr"]')
    oturum_ac.click()
    time.sleep(1)

    text = driver.find_element("xpath",'//*[@id="TableMessageSign"]/tbody/tr/td[2]')
    print(text.text)

    time.sleep(5)
    driver.close()
    return text