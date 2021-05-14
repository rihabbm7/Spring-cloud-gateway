package tn.itserv.model;



public class SasCampaign {
	
	private String sasCampaignCode;
	private int sasExecType;
	private String sasFileName;
	private String sasFileCreationDate;
	private int sasfileSize;
	private int totalLines;

	
	
	public String getSasCampaignCode() {
		return sasCampaignCode;
	}
	public void setSasCampaignCode(String sasCampaignCode) {
		this.sasCampaignCode = sasCampaignCode;
	}
	public int getSasExecType() {
		return sasExecType;
	}
	public void setSasExecType(int sasExecType) {
		this.sasExecType = sasExecType;
	}
	public String getSasFileName() {
		return sasFileName;
	}
	public void setSasFileName() {
		this.sasFileName = "FC_INPUT_"+sasCampaignCode+"_"+sasExecType+"_"+sasFileCreationDate+".csv";
	}
	public String getSasFileCreationDate() {
		return sasFileCreationDate;
	}

	public int getSasfileSize() {
		return sasfileSize;
	}
	public void setSasfileSize(int sasfileSize) {
		this.sasfileSize = sasfileSize;
	}
	public int getTotalLines() {
		return totalLines;
	}
	public void setTotalLines(int totalLines) {
		this.totalLines = totalLines;
	}

	

	public void setSasFileCreationDate(String strdate) {
		this.sasFileCreationDate = strdate;
	}
	
	
	public SasCampaign(String sasCampaignCode, int sasExecType, String sasFileName, String sasFileCreationDate,
			int sasfileSize, int totalLines) {
		super();
		this.sasCampaignCode = sasCampaignCode;
		this.sasExecType = sasExecType;
		this.sasFileName = sasFileName;
		this.sasFileCreationDate = sasFileCreationDate;
		this.sasfileSize = sasfileSize;
		this.totalLines = totalLines;
		
	}

	public SasCampaign() {
		
	}

	
	
}
