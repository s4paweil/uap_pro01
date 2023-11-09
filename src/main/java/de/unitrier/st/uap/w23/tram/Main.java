package de.unitrier.st.uap.w23.tram;



final class Main
{
	public static void main(String[] argv)
	{
		// No .tram-File given
		if(argv.length == 0){
			System.out.println("Error: Invalid input");
			System.exit(0);
		}

		boolean param = false;
		if(argv.length == 2) param = true;


		Instruction[] code;
		String settings = "";
		if(param){
			settings = argv[0];
			code = Assembler.readTRAMCode(argv[1]);
		}
		else{
			code = Assembler.readTRAMCode(argv[0]);
		}

		AbstractMachine tram;
		if(param){
			 tram = new AbstractMachine(code,settings);
		}
		else{
			 tram = new AbstractMachine(code);
		}
		tram.run();
	}

	//		Instruction[] code = Assembler.readTRAMCode(argv[0]
//				// "tramcode\\square.tram"
//				//"tramcode\\wrapper.tram"
//		         //"tramcode\\example1.tram"
//				//"tramcode\\example2.tram"
//				//"tramcode\\example3.tram"
//				//"tramcode\\test.tram"
//		);
}