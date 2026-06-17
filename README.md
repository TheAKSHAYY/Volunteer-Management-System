# 🌿 NayePankh Foundation — Volunteer Management System

> A Java Swing desktop application to digitally manage volunteer records for NayePankh Foundation NGO.
> Built as a Java Development Internship project demonstrating OOP, Collections, File Handling, GUI, and CRUD operations.


## 📸 Screenshots

### Dashboard
![Dashboard](screenshot/Screenshot%202026-06-16%20171943.png)

### Add Volunteer
![Add Volunteer](screenshot/Screenshot%202026-06-16%20172003.png)

### All Volunteers
![All Volunteers](screenshot/Screenshot%202026-06-16%20172022.png)

### Search Volunteer
![Search Volunteer](screenshot/Screenshot%202026-06-16%20172039.png)

### Reports & Analytics
![Reports](screenshot/Screenshot%202026-06-16%20172614.png)

---

## 🚀 Features

### Volunteer Management
- ✅ Register new volunteers with auto-generated ID (`NPF-XXXX`)
- ✅ View all volunteers in a sortable table
- ✅ Edit volunteer details via dialog
- ✅ Delete volunteers with confirmation
- ✅ Search by Volunteer ID or Name

### Data Persistence
- ✅ All records saved to `volunteers.txt` automatically
- ✅ Data loads on startup — no manual imports needed
- ✅ Pipe-separated value format for readability

### Reports
- ✅ Total volunteer count
- ✅ Volunteers grouped by city
- ✅ Recently added volunteers
- ✅ Export full report to a timestamped `.txt` file

### GUI
- ✅ Modern Java Swing interface
- ✅ Sidebar navigation
- ✅ Dashboard with stat cards
- ✅ Responsive card layout

---

## 🗂 Project Structure

```
NayePankh/
├── src/
│   ├── Main.java                    ← Entry point
│   ├── model/
│   │   └── Volunteer.java           ← Data model (POJO)
│   ├── service/
│   │   └── VolunteerService.java    ← Business logic (CRUD + Reports)
│   ├── util/
│   │   ├── FileHandler.java         ← File read/write
│   │   ├── InputValidator.java      ← Email & phone validation
│   │   └── UITheme.java             ← Central color/font constants
│   └── gui/
│       ├── DashboardFrame.java      ← Main window + sidebar
│       ├── HomePanel.java           ← Dashboard overview
│       ├── AddPanel.java            ← Register volunteer form
│       ├── ListPanel.java           ← All volunteers table
│       ├── SearchPanel.java         ← Search by ID / Name
│       └── ReportPanel.java         ← Reports + export
├── volunteers.txt                   ← Auto-created on first run
└── README.md
```

---

## ⚙️ Technologies Used

| Technology | Purpose |
|---|---|
| Core Java (JDK 8+) | Language |
| Java Swing | GUI Framework |
| ArrayList | In-memory data store |
| BufferedReader / BufferedWriter | File persistence |
| OOP (Encapsulation, Constructors, Getters/Setters) | Clean architecture |
| Regex (Pattern) | Email & phone validation |

---

## 🛠 Setup Instructions

### Prerequisites
- **JDK 8 or higher** installed
- **IntelliJ IDEA** or **Eclipse IDE**

---

### Option A — IntelliJ IDEA

1. Open IntelliJ IDEA → **File → New → Project from Existing Sources**
2. Select the `NayePankh` folder → choose **"Create project from existing sources"**
3. Keep clicking **Next** until the project is created
4. Right-click `src/Main.java` → **Run 'Main.main()'**

**Alternatively (recommended):**
1. **File → New → Java Project**
2. Name it `NayePankh`
3. Copy all source files into `src/` maintaining package folders
4. Right-click `Main.java` → **Run**

---

### Option B — Eclipse IDE

1. **File → New → Java Project** → Name: `NayePankh`
2. In the project, right-click `src` → **New → Package** → create:
   - `model`
   - `service`
   - `util`
   - `gui`
3. Copy each `.java` file into its corresponding package folder
4. Right-click `Main.java` → **Run As → Java Application**

---

### Option C — Command Line (javac)

```bash
# From the NayePankh/ directory:
cd src

# Compile all files
javac -d ../out model/Volunteer.java util/FileHandler.java util/InputValidator.java util/UITheme.java service/VolunteerService.java gui/DashboardFrame.java gui/HomePanel.java gui/AddPanel.java gui/ListPanel.java gui/SearchPanel.java gui/ReportPanel.java Main.java

# Run
cd ../out
java Main
```

---

## 📋 How to Use

| Action | Steps |
|---|---|
| Add Volunteer | Sidebar → **Add Volunteer** → fill form → **Register** |
| View All | Sidebar → **All Volunteers** |
| Edit | All Volunteers tab → select row → **Edit Selected** |
| Delete | All Volunteers tab → select row → **Delete Selected** |
| Search by ID | Search tab → select **Search by ID** → enter `NPF-XXXX` |
| Search by Name | Search tab → select **Search by Name** → enter partial name |
| Generate Report | Reports tab → **Generate Report** |
| Export Report | Reports tab → **Export to File** (saves as `NayePankh_Report_TIMESTAMP.txt`) |

---

## 📦 Data Format

Volunteer data is stored in `volunteers.txt` in the following format:

```
NPF-1001|Rahul Sharma|rahul@gmail.com|9876543210|Delhi|Teaching, Content Writing|15-06-2024
NPF-1002|Priya Mehta|priya@outlook.com|8765432109|Mumbai|Design, Social Media|16-06-2024
```

---

## ✅ OOP Concepts Demonstrated

| Concept | Where Used |
|---|---|
| **Encapsulation** | `Volunteer.java` — private fields + getters/setters |
| **Constructors** | `Volunteer.java` — overloaded constructors |
| **Abstraction** | `VolunteerService.java` — hides data logic from GUI |
| **Inheritance** | All GUI panels extend `JPanel` |
| **Collections** | `ArrayList<Volunteer>` in service layer |
| **Exception Handling** | FileHandler try-catch blocks |
| **Serialization** | `Volunteer implements Serializable` |

---

## 👨‍💻 Author

**[akshay sharma ]**
Java Development Intern — NayePankh Foundation
📧 [akshay sharma]
🔗 [https://www.linkedin.com/in/akkshay-sharma/]

---

## 📄 License

This project is created for educational and internship submission purposes.
