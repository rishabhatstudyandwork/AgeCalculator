import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class AgeCalculator extends JFrame {
    private JTextField birthDateField;
    private JLabel resultLabel, zodiacLabel, nextBirthdayLabel, leapYearLabel, ageInUnitsLabel;

    public AgeCalculator() {
        setTitle("Age Calculator");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1, 10, 10));

        // Colors
        Color turquoise = new Color(64, 224, 208);
        Color coral = new Color(255, 127, 80);

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(turquoise);
        inputPanel.add(new JLabel("Enter Birth Date (YYYY-MM-DD):"));
        birthDateField = new JTextField(10);
        inputPanel.add(birthDateField);

        JButton calculateButton = new JButton("Calculate Age");
        calculateButton.setBackground(coral);
        calculateButton.setForeground(Color.WHITE);
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateAge();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(turquoise);
        buttonPanel.add(calculateButton);

        resultLabel = new JLabel("Your age will appear here.", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));

        zodiacLabel = new JLabel("Zodiac Sign: ", SwingConstants.CENTER);
        zodiacLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        nextBirthdayLabel = new JLabel("Days until next birthday: ", SwingConstants.CENTER);
        nextBirthdayLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        leapYearLabel = new JLabel("Leap Year Info: ", SwingConstants.CENTER);
        leapYearLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        ageInUnitsLabel = new JLabel("Age in different units: ", SwingConstants.CENTER);
        ageInUnitsLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        add(inputPanel);
        add(buttonPanel);
        add(resultLabel);
        add(zodiacLabel);
        add(nextBirthdayLabel);
        add(leapYearLabel);
        add(ageInUnitsLabel);
    }

    private void calculateAge() {
        try {
            LocalDate birthDate = LocalDate.parse(birthDateField.getText());
            LocalDate currentDate = LocalDate.now();
            Period age = Period.between(birthDate, currentDate);

            resultLabel.setText("You are " + age.getYears() + " years, " + age.getMonths() + " months, and " + age.getDays() + " days old.");

            // Zodiac Sign Calculation via API
            zodiacLabel.setText("Zodiac Sign: " + getZodiacSignFromAPI(birthDate));

            // Days until next birthday
            LocalDate nextBirthday = birthDate.withYear(currentDate.getYear());
            if (nextBirthday.isBefore(currentDate) || nextBirthday.equals(currentDate)) {
                nextBirthday = nextBirthday.plusYears(1);
            }
            long daysUntilBirthday = ChronoUnit.DAYS.between(currentDate, nextBirthday);
            nextBirthdayLabel.setText("Days until next birthday: " + daysUntilBirthday);

            // Leap Year Check
            leapYearLabel.setText("Leap Year: " + (birthDate.isLeapYear() ? "Yes" : "No"));

            // Age in Different Units
            long totalDays = ChronoUnit.DAYS.between(birthDate, currentDate);
            long totalWeeks = totalDays / 7;
            long totalMonths = ChronoUnit.MONTHS.between(birthDate, currentDate);
            long totalHours = totalDays * 24;
            ageInUnitsLabel.setText("Age: " + totalMonths + " months, " + totalWeeks + " weeks, " + totalDays + " days, " + totalHours + " hours");
        } catch (Exception e) {
            resultLabel.setText("Invalid date format. Use YYYY-MM-DD.");
        }
    }

    private String getZodiacSignFromAPI(LocalDate birthDate) {
        try {
            String apiUrl = "https://some-zodiac-api.com/getZodiac?date=" + birthDate.toString();
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString(); // Assuming the API returns just the zodiac sign
        } catch (Exception e) {
            return "Error fetching zodiac";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AgeCalculator().setVisible(true);
        });
    }
}