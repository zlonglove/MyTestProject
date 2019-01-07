package zlonglove.cn.voice;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class VoiceTools {

	public static void BuffersToAmrFile(List<byte[]> amrList, String filePath) throws IOException {
		if(amrList == null || amrList.size() == 0) {
			return;
		}
		FileOutputStream fs = new FileOutputStream(filePath);
		byte[] header = ("#!AMR\n").getBytes();
		fs.write(header);
		for( byte[] amr : amrList)
		{
			if(amr != null && amr.length > 0) {
				fs.write(amr);
				fs.flush();
			}
		}
		fs.close();
	}
	
	public static byte[] getAmrFileDatas(String filePath){
		FileInputStream fis = null;
		byte[] datas = null;
		try {
			fis = new FileInputStream(filePath);

			byte[] header = new byte[6];
			fis.read(header);
			String strheader = new String(header);
			if (!strheader.equals("#!AMR\n")) {
				return null;
			}
		}
		catch (FileNotFoundException e) {
			return null;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		int size;
		try {
			size = fis.available();
			datas = new byte[size];
			fis.read(datas);
			fis.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return datas;
	}
	
	public static int getAmrDatasLength(String filePath)
	{
		File file = new File(filePath);
		int length = (int)file.length();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);

			byte[] header = new byte[6];
			fis.read(header);
			String strheader = new String(header);
			if (strheader.equals("#!AMR\n")) {
				length -= 6;
			}
		}
		catch (FileNotFoundException e) {
			return 0;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

		return length;
	}
	
	public static ArrayList<byte[]> getAmrDatas(String filePath) {
		FileInputStream fis = null;
		ArrayList<byte[]> amrDatas = null;
		try {
			fis = new FileInputStream(filePath);
			int size = fis.available();
			if (size <= 6) {
				return null;
			}
			byte[] header = new byte[6];
			fis.read(header);
			String strheader = new String(header);
			if (!strheader.equals("#!AMR\n")) {
				return null;
			}

			int frameSize = 1000;
			int inputSize = 0;
			amrDatas = new ArrayList<byte[]>();
			for (int i = size - 6; i > 0; i -= inputSize) {
				inputSize = Math.min(frameSize * 50, i);
				byte[] amr = new byte[inputSize];
				fis.read(amr);
				amrDatas.add(amr);
			}
			return amrDatas;
		}
		catch (FileNotFoundException e) {

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (fis != null) {
				try {
					fis.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
