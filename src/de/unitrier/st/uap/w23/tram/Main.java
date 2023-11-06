package de.unitrier.st.uap.w23.tram;

final class Main
{
	private Main(){}
	
	public static void main(String[] argv)
	{
		Instruction[] code = Assembler.readTRAMCode(
				// "tramcode\\square.tram"
				// "tramcode\\wrapper.tram"
		        // "tramcode\\example1.tram"
				// "tramcode\\example2.tram"
				//"tramcode\\example3.tram"
				"tramcode\\test.tram"
		);

		int lineNr=0;
		for(Instruction instr: code) {
			if (instr != null) {
				System.out.println(String.format("%03d", lineNr) + "| " + instr.toString());
				lineNr++;
			}
		}

		// TODO: Create an instance of the abstract machine with respective parameters
	}
}