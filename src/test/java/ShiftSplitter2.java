import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Shift {
    String userId;
    LocalDateTime startTime;
    LocalDateTime endTime;
    String shift;

    public Shift(String userId, String startTime, String endTime, String shift) {
        this.userId = userId;
        this.startTime = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.endTime = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.shift = shift;
    }

    public Shift(String userId, LocalDateTime startTime, LocalDateTime endTime, String shift) {
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.shift = shift;
    }

    @Override
    public String toString() {
        return "Shift{" +
                "userId='" + userId + '\'' +
                ", startTime=" + startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                ", endTime=" + endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                ", shift='" + shift + '\'' +
                '}';
    }
}

public class ShiftSplitter2 {
    public static void main(String[] args) {
        List<Shift> shifts = new ArrayList<>();
        shifts.add(new Shift("user01", "2023-10-12 08:00:00", "2023-10-12 18:00:00", "working"));
        shifts.add(new Shift("user01", "2023-10-12 09:00:00", "2023-10-12 09:30:00", "eat"));
        shifts.add(new Shift("user01", "2023-10-12 11:00:00", "2023-10-12 11:30:00", "exercise"));
        shifts.add(new Shift("user01", "2023-10-12 15:00:00", "2023-10-12 15:30:00", "tea"));
        shifts.add(new Shift("user02", "2023-10-13 09:00:00", "2023-10-13 09:30:00", "jogging"));
        shifts.add(new Shift("user02", "2023-10-13 13:00:00", "2023-10-13 14:30:00", "meeting"));

        List<Shift> result = new ArrayList<>();

        // Process each user
        List<String> userIds = shifts.stream().map(shift -> shift.userId).distinct().collect(Collectors.toList());

        for (String userId : userIds) {
            List<Shift> userShifts = shifts.stream()
                    .filter(shift -> shift.userId.equals(userId))
                    .sorted(Comparator.comparing(shift -> shift.startTime))
                    .collect(Collectors.toList());

            List<Shift> smallShifts = new ArrayList<>();
            List<Shift> generalShifts = new ArrayList<>();

            // Determine general shifts and small shifts
            for (Shift shift : userShifts) {
                boolean isGeneralShift = userShifts.stream()
                        .anyMatch(s -> s != shift && s.startTime.isAfter(shift.startTime) && s.endTime.isBefore(shift.endTime));
                if (isGeneralShift) {
                    generalShifts.add(shift);
                } else {
                    smallShifts.add(shift);
                }
            }

            if (generalShifts.isEmpty()) {
                result.addAll(smallShifts);
            } else {
                for (Shift generalShift : generalShifts) {
                    List<Shift> overlappingSmallShifts = smallShifts.stream()
                            .filter(smallShift -> smallShift.startTime.isBefore(generalShift.endTime) && smallShift.endTime.isAfter(generalShift.startTime))
                            .collect(Collectors.toList());
                    splitShift(userId, generalShift, overlappingSmallShifts, result);
                }
            }
        }

        // Print the result
        result.stream()
                .sorted(Comparator.comparing(shift -> shift.startTime))
                .forEach(System.out::println);
    }

    private static void splitShift(String userId, Shift generalShift, List<Shift> smallShifts, List<Shift> result) {
        LocalDateTime currentStart = generalShift.startTime;
        for (Shift smallShift : smallShifts) {
            if (currentStart.isBefore(smallShift.startTime)) {
                result.add(new Shift(userId, currentStart, smallShift.startTime, generalShift.shift));
            }
            result.add(new Shift(userId, smallShift.startTime, smallShift.endTime, smallShift.shift));
            currentStart = smallShift.endTime;
        }
        if (currentStart.isBefore(generalShift.endTime)) {
            result.add(new Shift(userId, currentStart, generalShift.endTime, generalShift.shift));
        }
    }
}

