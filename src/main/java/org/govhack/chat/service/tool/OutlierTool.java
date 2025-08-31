package org.govhack.chat.service.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.List;

public class OutlierTool {


    // Define a record to wrap the list of data points
    public record OutlierDetectionInput(
            @ToolParam(description = "A list of numerical values to analyze")
            List<Double> data
    ) {}

    // Your tool method now accepts the record as a single parameter
    @Tool(description = "detects outliers in a dataset", name = "outlierDetector")
    public String detectOutliers(OutlierDetectionInput input) {
        List<Double> data = input.data();
        // ... your outlier detection logic here
        // ...
        return "Outliers detected: " + data;
    }
}
