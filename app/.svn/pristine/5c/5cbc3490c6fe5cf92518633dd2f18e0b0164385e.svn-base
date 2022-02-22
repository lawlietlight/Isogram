package com.efficientsciences.cowsandbulls.wordwars.domain;

import java.util.SortedSet;
import java.util.TreeSet;

public class Page implements Comparable<Page>{


	private int pageIndex;
	private SortedSet<PageChunk> chunks=new TreeSet<PageChunk>();


	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public SortedSet<PageChunk> getChunks() {
		return chunks;
	}

	public void setChunks(SortedSet<PageChunk> chunks) {
		this.chunks = chunks;
	}
	
	public boolean addPageChunks(PageChunk pageChunk){
		int chunkIndex = 0;
		if(null!=this.chunks && !this.chunks.isEmpty()){
			chunkIndex= this.chunks.size()+1;
		}
		pageChunk.setIndex(chunkIndex);

		return this.chunks.add(pageChunk);
	}
	
	@Override
	public int compareTo(Page another) {
		if(this.getPageIndex()<another.getPageIndex()){
			return -1;
		}
		else if(this.getPageIndex() > another.getPageIndex()){
			return 1;
		}
		return 0;
	}

	public boolean hasPageChunk(PageChunk p) {
		for (PageChunk chunk:chunks) {
			if(chunk.equals(p)){
				return true;
			}
		}
		return false;
	}

	



}
