import java.util.ArrayList;
import java.util.Random;

public class StockManipulator extends Thread{
    @Override
    public void run() {
        Random random = new Random();
        ArrayList<Stock> stocks = DbHelper.getStocks();
        int[] arr = {1,0,2,0,3,0,4,0,5,0,-1,0,-2,0,-3};
        while(true){

            for (Stock stock : stocks){  
                double x = (arr[random.nextInt(15)]/100.0) * stock.getPrice() * (random.nextBoolean() ? -1 : 1);
                if(x!=0){
                    stock.updatePrice(x);
                    DbHelper.updateStockValue(stock);
                }
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {}
        
        }
    }
}
