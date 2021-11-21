public class BellmanFordCheapestKFlights {

    public static void main(String[] args) {
        int[][] flights = {{0, 1, 100}, {1, 2, 100}, {0, 2, 500}};
        int n = flights.length;
        int source = 0;
        int dest = 2;
        int stops = 1;

        System.out.println(findCheapestPrice(n, flights, source, dest, stops)); // 200

        stops = 0;
        System.out.println(findCheapestPrice(n, flights, source, dest, stops)); // 500
    }

    public static int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        // Array containing min prices
        int[] price = new int[n];

        // Initialize prices to max value to simulate "inf"
        for (int i = 0; i < price.length; i++)
            price[i] = Integer.MAX_VALUE;

        // Min price to source is 0
        price[src] = 0;

        // At k-th iteration, we will have stopped k-1 times,
        // so to find the cheapest price in k stops,
        // we must relax k+1 times.
        for (int i = 0; i < k + 1; i++) {
            int[] temp = price.clone();

            // For each edge from u->v, relax if tense
            for (int j = 0; j < flights.length; j++) {
                int u = flights[j][0];
                int v = flights[j][1];
                int uvPrice = flights[j][2];

                // If edge u->v is tense, relax the edge
                if (price[u] != Integer.MAX_VALUE) {
                    temp[v] = (temp[v] < price[u] + uvPrice) ?
                            temp[v] :
                            price[u] + uvPrice;
                }
            }
            price = temp;
        }

        return price[dst] == Integer.MAX_VALUE ? -1 : price[dst];
    }
}
