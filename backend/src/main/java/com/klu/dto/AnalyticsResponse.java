package com.klu.dto;

public class AnalyticsResponse {
    private long totalFoodItems;
    private long availableFoodItems;
    private long claimedFoodItems;
    private long totalRequests;
    private long pendingRequests;
    private long approvedRequests;
    private long rejectedRequests;
    private long totalCompletedDonations;

    public AnalyticsResponse() {}

    public AnalyticsResponse(long totalFoodItems, long availableFoodItems, long claimedFoodItems,
                             long totalRequests, long pendingRequests, long approvedRequests,
                             long rejectedRequests, long totalCompletedDonations) {
        this.totalFoodItems = totalFoodItems;
        this.availableFoodItems = availableFoodItems;
        this.claimedFoodItems = claimedFoodItems;
        this.totalRequests = totalRequests;
        this.pendingRequests = pendingRequests;
        this.approvedRequests = approvedRequests;
        this.rejectedRequests = rejectedRequests;
        this.totalCompletedDonations = totalCompletedDonations;
    }

    public long getTotalFoodItems() { return totalFoodItems; }
    public void setTotalFoodItems(long totalFoodItems) { this.totalFoodItems = totalFoodItems; }

    public long getAvailableFoodItems() { return availableFoodItems; }
    public void setAvailableFoodItems(long availableFoodItems) { this.availableFoodItems = availableFoodItems; }

    public long getClaimedFoodItems() { return claimedFoodItems; }
    public void setClaimedFoodItems(long claimedFoodItems) { this.claimedFoodItems = claimedFoodItems; }

    public long getTotalRequests() { return totalRequests; }
    public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }

    public long getPendingRequests() { return pendingRequests; }
    public void setPendingRequests(long pendingRequests) { this.pendingRequests = pendingRequests; }

    public long getApprovedRequests() { return approvedRequests; }
    public void setApprovedRequests(long approvedRequests) { this.approvedRequests = approvedRequests; }

    public long getRejectedRequests() { return rejectedRequests; }
    public void setRejectedRequests(long rejectedRequests) { this.rejectedRequests = rejectedRequests; }

    public long getTotalCompletedDonations() { return totalCompletedDonations; }
    public void setTotalCompletedDonations(long totalCompletedDonations) { this.totalCompletedDonations = totalCompletedDonations; }
}