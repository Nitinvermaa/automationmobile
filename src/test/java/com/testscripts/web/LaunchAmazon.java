package com.testscripts.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class LaunchAmazon {
    public static void main(String[] args) {
        // Set the path to chromedriver executable if needed
        // System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");

        // Setup ChromeDriver using WebDriverManager (optional but recommended)
        WebDriverManager.chromedriver().setup();

        // Create ChromeDriver instance
        WebDriver driver = new ChromeDriver();

        // Maximize browser window
        driver.manage().window().maximize();

        // Open Amazon.com
        driver.get("https://www.amazon.com/");

        // Print the title of the page
        System.out.println("Page title is: " + driver.getTitle());

        // Close the browser
        driver.quit();



    }
}

