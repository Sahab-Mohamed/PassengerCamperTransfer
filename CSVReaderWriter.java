package PassengerCamperTransferPackage;

import java.io.*;
import java.util.Scanner;
import queues.BoundedQueue;
import queues.QueueOverflowException;
import queues.QueueUnderflowException;


public class CSVReaderWriter {

    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        displayMenu();
        boolean notExit = true;
        while (notExit) {
            System.out.println("Do you want to unload trains again? (Yes/No)");
            String choice = scnr.next();

            if (choice.equalsIgnoreCase("Yes")) {
                displayMenu();
            } else if (choice.equalsIgnoreCase("No")) {
                scnr.close();
            	notExit = false;
            } else {
                System.out.println("Invalid entry!");
            }

        }
    }

    public static void displayMenu() {
        @SuppressWarnings("resource")
		Scanner scnr = new Scanner(System.in);
        int y12s, y13s, y14s, y15s, y16s, y17s;
        System.out.println("Please set the size for each age/gender group (at least two campers).");
        System.out.println("size for 12 years-old: ");
        y12s = scnr.nextInt();
        System.out.println("size for 13 years-old: ");
        y13s = scnr.nextInt();
        System.out.println("size for 14 years-old: ");
        y14s = scnr.nextInt();
        System.out.println("size for 15 years-old: ");
        y15s = scnr.nextInt();
        System.out.println("size for 16 years-old: ");
        y16s = scnr.nextInt();
        System.out.println("size for 17 years-old: ");
        y17s = scnr.nextInt();

        csvReaderWriter(y12s, y13s, y14s, y15s, y16s, y17s);
    }

    
    public static void csvReaderWriter(int y12s, int y13s, int y14s, int y15s, int y16s, int y17s) {
        BoundedQueue<Camper> train12YoMale = new BoundedQueue<>(y12s);
        BoundedQueue<Camper> train12YoFemale = new BoundedQueue<>(y12s);
        BoundedQueue<Camper> train13YoMale = new BoundedQueue<>(y13s);
        BoundedQueue<Camper> train13YoFemale = new BoundedQueue<>(y13s);
        BoundedQueue<Camper> train14YoMale = new BoundedQueue<>(y14s);
        BoundedQueue<Camper> train14YoFemale = new BoundedQueue<>(y14s);
        BoundedQueue<Camper> train15YoMale = new BoundedQueue<>(y15s);
        BoundedQueue<Camper> train15YoFemale = new BoundedQueue<>(y15s);
        BoundedQueue<Camper> train16YoMale = new BoundedQueue<>(y16s);
        BoundedQueue<Camper> train16YoFemale = new BoundedQueue<>(y16s);
        BoundedQueue<Camper> train17YoMale = new BoundedQueue<>(y17s);
        BoundedQueue<Camper> train17YoFemale = new BoundedQueue<>(y17s);

        String fileRead = "C:\\Users\\CSC 223 - Fall 2023\\Assignments workspace\\PassengerCamperTransferProject\\src\\PassengerCamperTransferPackage\\TrainFile5000.csv";
        String maleCampers = "C:\\Users\\CSC 223 - Fall 2023\\Assignments workspace\\PassengerCamperTransferProject\\src\\PassengerCamperTransferPackage\\maleCampers.csv";
        String femaleCampers = "C:\\Users\\CSC 223 - Fall 2023\\Assignments workspace\\PassengerCamperTransferProject\\src\\PassengerCamperTransferPackage\\femaleCampers.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileRead));
             FileWriter maleCampersWriter = new FileWriter(maleCampers);
             FileWriter femaleCampersWriter = new FileWriter(femaleCampers);
             // Add similar FileWriter declarations for other trains
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");

                Camper c = new Camper();
                int camperAge = Integer.parseInt(row[3]);

                if (camperAge >= 12 && camperAge <= 17) {
                    BoundedQueue<Camper> maleQueue = null;
                    BoundedQueue<Camper> femaleQueue = null;

                    switch (camperAge) {
                        case 12:
                            maleQueue = train12YoMale;
                            femaleQueue = train12YoFemale;
                            break;
                        case 13:
                            maleQueue = train13YoMale;
                            femaleQueue = train13YoFemale;
                            break;
                        case 14:
                            maleQueue = train14YoMale;
                            femaleQueue = train14YoFemale;
                            break;
                        case 15:
                            maleQueue = train15YoMale;
                            femaleQueue = train15YoFemale;
                            break;
                        case 16:
                            maleQueue = train16YoMale;
                            femaleQueue = train16YoFemale;
                            break;
                        case 17:
                            maleQueue = train17YoMale;
                            femaleQueue = train17YoFemale;
                            break;
                        default:
                            break;
                    }

                    try {
                        if ((maleQueue != null && maleQueue.isFull()) || (femaleQueue != null && femaleQueue.isFull())) {
                            // If either male or female queue is full, unload both queues
                            try {
                                unloadTrain(train12YoMale, maleCampersWriter, femaleCampersWriter, train12YoMale.size());
                                unloadTrain(train12YoFemale, maleCampersWriter, femaleCampersWriter, train12YoFemale.size());
                            } catch (QueueUnderflowException e) {
                                e.printStackTrace();
                            }
                        }

                        if (maleQueue != null && maleQueue.isFull()) {
                            try {
								unloadTrain(maleQueue, maleCampersWriter, femaleCampersWriter, maleQueue.size());
							} catch (QueueUnderflowException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        } else if (femaleQueue != null && femaleQueue.isFull()) {
                            try {
								unloadTrain(femaleQueue, maleCampersWriter, femaleCampersWriter, femaleQueue.size());
							} catch (QueueUnderflowException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        }

                        // Insert the camper into the proper train
                        handleCamper(c, row, maleQueue, femaleQueue);

                    } catch (QueueOverflowException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Unload any remaining campers in the queues
            try {
				unloadTrain(train12YoMale, maleCampersWriter, femaleCampersWriter, train12YoMale.size());
			} catch (QueueUnderflowException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				unloadTrain(train12YoFemale, maleCampersWriter, femaleCampersWriter, train12YoFemale.size());
			} catch (QueueUnderflowException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				unloadTrain(train13YoMale, maleCampersWriter, femaleCampersWriter, train13YoMale.size());
			} catch (QueueUnderflowException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				unloadTrain(train13YoFemale, maleCampersWriter, femaleCampersWriter, train13YoFemale.size());
			} catch (QueueUnderflowException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				unloadTrain(train14YoMale, maleCampersWriter, femaleCampersWriter, train14YoMale.size());
			} catch (QueueUnderflowException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				unloadTrain(train14YoFemale, maleCampersWriter, femaleCampersWriter, train14YoFemale.size());
			} catch (QueueUnderflowException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				unloadTrain(train15YoMale, maleCampersWriter, femaleCampersWriter, train15YoMale.size());
			} catch (QueueUnderflowException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				unloadTrain(train15YoFemale, maleCampersWriter, femaleCampersWriter, train15YoFemale.size());
			} catch (QueueUnderflowException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				unloadTrain(train16YoMale, maleCampersWriter, femaleCampersWriter, train16YoMale.size());
			} catch (QueueUnderflowException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				unloadTrain(train16YoFemale, maleCampersWriter, femaleCampersWriter, train16YoFemale.size());
			} catch (QueueUnderflowException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				unloadTrain(train17YoMale, maleCampersWriter, femaleCampersWriter, train17YoMale.size());
			} catch (QueueUnderflowException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				unloadTrain(train17YoFemale, maleCampersWriter, femaleCampersWriter, train17YoFemale.size());
			} catch (QueueUnderflowException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void handleCamper(Camper c, String[] row, BoundedQueue<Camper> maleTrain, BoundedQueue<Camper> femaleTrain) throws QueueOverflowException
    {
    		c.setLast(row[0]);
    		c.setFirst(row[1]);
    		c.setMiddle(row[2]);
    		c.setAge(Integer.parseInt(row[3]));
    		c.setGender(row[4]);
    		c.setStatus(row[5]);
    		c.setID(row[6]);
    		
    		if (row[4].charAt(0) == 'M') {
    				if (!maleTrain.isFull())
    				maleTrain.enqueue(c);
    				
    		} else {
    			if (!femaleTrain.isFull())
    				femaleTrain.enqueue(c);
    			
    			}
    }

    private static void unloadTrain(BoundedQueue<Camper> train, FileWriter maleCampersWriter, FileWriter femaleCampersWriter, int tentSize)
            throws IOException, QueueUnderflowException {
    	int actualSize = train.size();
        while (!train.isEmpty()) {
            
            int count = 0;
            Camper trainHeader = train.peek();
            if(trainHeader.getGender().equals("M")) {
            	maleCampersWriter.append("Age: "+trainHeader.getAge() + "," + "Gender:" +trainHeader.getGender()+ "," + "Group Size: "+actualSize+ "\n");
            }
            else {
            	femaleCampersWriter.append("Age: " + trainHeader.getAge() + "," +  "Gender:" +trainHeader.getGender()+"Group Size: "+ actualSize+ "\n");
            }
            
            while (count < tentSize && !train.isEmpty()) {
                Camper camper = train.dequeue();
                if (camper.getGender().equals("M")) {
                    maleCampersWriter.append(camper.getLast() + "," + camper.getMiddle()+ "," + camper.getFirst() + "\n");
                } else {
                    femaleCampersWriter.append(camper.getLast() + "," + camper.getMiddle()+ "," + camper.getFirst() + "\n");
                }
                count++;
            }
        }

    }

}
	


