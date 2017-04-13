package com.binana.amas.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.binana.amas.domain.Amember;
import com.binana.amas.domain.CollegePie;
import com.binana.amas.domain.CommBean;
import com.binana.amas.domain.GenderPie;
import com.binana.amas.repository.AmemberRepository;
import com.binana.amas.repository.search.AmemberSearchRepository;
import com.binana.amas.web.rest.util.ExcelUtil;
import com.binana.amas.web.rest.util.HeaderUtil;
import com.binana.amas.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Amember.
 */
@RestController
@RequestMapping("/api")
public class AmemberResource {

    private final Logger log = LoggerFactory.getLogger(AmemberResource.class);

    private static final String ENTITY_NAME = "amember";
        
    private final AmemberRepository amemberRepository;

    private final AmemberSearchRepository amemberSearchRepository;
    
    @Autowired
    private  ExcelUtil excelUtil;

    public AmemberResource(AmemberRepository amemberRepository, AmemberSearchRepository amemberSearchRepository) {
        this.amemberRepository = amemberRepository;
        this.amemberSearchRepository = amemberSearchRepository;
    }
    
    /**
     * 获取某一社团会员性别比例分析饼图
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/allAmembersGradePie/{id}")
    @Timed
    public List<CommBean> getAssoAmembersGradePie(@PathVariable Long id) 
    		throws URISyntaxException{
    	log.debug("REST request 获取某一社团会员各个年级分析饼图 ");
    	List<CommBean> cpList = new ArrayList<CommBean>();
    	cpList = amemberRepository.countGradeByAssoId(id);
    	return cpList;
    }
    
    /**
     * 获取全体会员各个年级分布分析饼图
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/allAmembersGradePie")
    @Timed
    public List<CommBean> getAllAmembersGradePie() 
    		 throws URISyntaxException{
    	log.debug("REST request 获取全体会员专业分布分析饼图 ");
    	List<CommBean> cpList = new ArrayList<CommBean>();
    	cpList = amemberRepository.countGradeMapResult();
    	return cpList;
    }
    
    /**
     * 获取某一社团会员性别比例分析饼图
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/allAmembersGenderPie/{id}")
    @Timed
    public List<GenderPie> getAssoAmembersGenderPie(@PathVariable Long id) 
    		throws URISyntaxException{
    	log.debug("REST request 获取某一社团会员性别比例分析饼图 ");
    	List<GenderPie> cpList = new ArrayList<GenderPie>();
    	cpList = amemberRepository.countGenderByAssoId(id);
    	//汉化
    	for(int i=0;i<cpList.size();i++){
    		cpList.get(i).setChinese();
    	}
    	return cpList;
    }
    
    /**
     * 获取全体会员性别比例分析饼图
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/allAmembersGenderPie")
    @Timed
    public List<GenderPie> getAllAmembersGenderPie() 
    		 throws URISyntaxException{
    	log.debug("REST request 获取全体会员专业分布分析饼图 ");
    	List<GenderPie> cpList = new ArrayList<GenderPie>();
    	cpList = amemberRepository.countGenderMapResult();
    	//汉化
    	for(int i=0;i<cpList.size();i++){
    		cpList.get(i).setChinese();
    	}
    	return cpList;
    }
    
    /**
     * 获取某一社团会员专业分布分析饼图
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/allAmembersMajorPie/{id}")
    @Timed
    public List<CommBean> getAssoAmembersMajorPie(@PathVariable Long id) 
    		throws URISyntaxException{
    	log.debug("REST request 获取全体会员专业分布分析饼图 ");
    	List<CommBean> cpList = new ArrayList<CommBean>();
    	cpList = amemberRepository.countMajorByAssoId(id);
    	return cpList;
    }
    
    /**
     * 获取全体会员前10专业分布分析饼图
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/allAmembersMajorPie")
    @Timed
    public List<CommBean> getAllAmembersMajorPie() 
    		 throws URISyntaxException{
    	log.debug("REST request 获取全体会员专业分布分析饼图 ");
    	List<CommBean> cpList = new ArrayList<CommBean>();
    	cpList = amemberRepository.countMajorMapResult();
    	return cpList;
    }
    
    /**
     * 获取某一社团会员学院分布分析饼图
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/allAmembersCollegePie/{id}")
    @Timed
    public List<CollegePie> getAssoAmembersCollegePie(@PathVariable Long id) 
    		throws URISyntaxException{
    	log.debug("REST request 获取全体会员学院分布分析饼图 ");
    	List<CollegePie> cpList = new ArrayList<CollegePie>();
    	cpList = amemberRepository.countCollegeByAssoId(id);
    	for(int i=0;i<cpList.size();i++){
    		cpList.get(i).setChineseName();
    	}
    	return cpList;
    }
    /**
     * 获取全体会员学院分布分析饼图
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/allAmembersCollegePie")
    @Timed
    public List<CollegePie> getAllAmembersCollegePie() 
    		 throws URISyntaxException{
    	log.debug("REST request 获取全体会员学院分布分析饼图 ");
    	List<CollegePie> cpList = new ArrayList<CollegePie>();
    	cpList = amemberRepository.countCollegeMapResult();
    	for(int i=0;i<cpList.size();i++){
    		cpList.get(i).setChineseName();
    	}
    	return cpList;
    }
    
    /**
     * 通过Excel文件批量导入会员
     * @param id
     * @param exportFiles
     * @return
     */
    @PostMapping("/fileUpload/{id}")
    @Timed
    public String fileUpload(@PathVariable Long id,@RequestParam("file") MultipartFile[] exportFiles) {
        log.debug("REST request to save Amember : {}");
        System.out.println(exportFiles[0].getName()+"---------"+id);
        
        try {
        	for(MultipartFile file:exportFiles ){
        		List<Amember> amemberList = excelUtil.parseExcel(file.getInputStream(),id);
        		amemberRepository.save(amemberList);
        		amemberSearchRepository.save(amemberList);
        	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return "hello";
    }
    /**
     * SEARCH  /_search/amembers?query=:query : search for the amember corresponding
     * to the query.
     *
     * @param query the query of the amember search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/amembersOfAsso")
    @Timed
    public ResponseEntity<List<Amember>> searchAmembersOfAsso(@RequestParam(value="assoId") Long id,@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Amembers Of Association for query {} and Association is {}", query,id);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
        		.must(QueryBuilders.termQuery("associations.id",id))
        		.must(queryStringQuery(query));
        Page<Amember> page = amemberSearchRepository.search(boolQueryBuilder, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/amembers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * Get 获取该社团的成员数量
     * @param id
     * @return
     */
    @GetMapping("/getAssoAmemberNum/{id}")
    @Timed
    public int getAssoAmemberNum(@PathVariable Long id) {
    	log.debug("REST request to get Number of Amembers of Association id : {}", id);
    	int count = amemberRepository.countByAssociations_Id(id);
    	return count;
    }
    
    /**
     * GET  /amembers/:id : get the "id" amember.
     *
     * @param id the id of the amember to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the amember, or with status 404 (Not Found)
     */
    @GetMapping("/amembersOfAsso/{id}")
    @Timed
    public ResponseEntity<List<Amember>> getByAssoId(@PathVariable Long id,@ApiParam Pageable pageable) 
    		 throws URISyntaxException{
    	log.debug("REST request to get a page of Amembers by AssociationId {}",id);
    	Page<Amember> page = amemberRepository.findByAssociations_Id(id,pageable);
         HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/amembersOfAsso");
         return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * POST  /amembers : Create a new amember.
     *
     * @param amember the amember to create
     * @return the ResponseEntity with status 201 (Created) and with body the new amember, or with status 400 (Bad Request) if the amember has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/amembers")
    @Timed
    public ResponseEntity<Amember> createAmember(@Valid @RequestBody Amember amember) throws URISyntaxException {
        log.debug("REST request to save Amember : {}", amember);
        if (amember.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new amember cannot already have an ID")).body(null);
        }
        Amember result = amemberRepository.save(amember);
        amemberSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/amembers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    /**
     * POST  /amembers : 新会员加入社团.
     *
     * @param amember the amember to create
     * @return the ResponseEntity with status 201 (Created) and with body the new amember, or with status 400 (Bad Request) if the amember has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/joinAmembers")
    @Timed
    public String joinAmember(@Valid @RequestBody Amember amember) throws URISyntaxException {
    	log.debug("REST request to save Amember : {}", amember);
    	amember.setStatus(-1);
    	Amember result = amemberRepository.save(amember);
    	Amember save = amemberSearchRepository.save(result);
    	return "加入成功";
    }

    /**
     * PUT  /amembers : Updates an existing amember.
     *
     * @param amember the amember to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated amember,
     * or with status 400 (Bad Request) if the amember is not valid,
     * or with status 500 (Internal Server Error) if the amember couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/amembers")
    @Timed
    public ResponseEntity<Amember> updateAmember(@Valid @RequestBody Amember amember) throws URISyntaxException {
        log.debug("REST request to update Amember : {}", amember);
        if (amember.getId() == null) {
            return createAmember(amember);
        }
        Amember result = amemberRepository.save(amember);
        amemberSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, amember.getId().toString()))
            .body(result);
    }

    /**
     * GET  /amembers : get all the amembers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of amembers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/amembers")
    @Timed
    public ResponseEntity<List<Amember>> getAllAmembers(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Amembers");
        Page<Amember> page = amemberRepository.findByStatusIsNull(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/amembers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /amembers/:id : get the "id" amember.
     *
     * @param id the id of the amember to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the amember, or with status 404 (Not Found)
     */
    @GetMapping("/amembers/{id}")
    @Timed
    public ResponseEntity<Amember> getAmember(@PathVariable Long id) {
        log.debug("REST request to get Amember : {}", id);
        Amember amember = amemberRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(amember));
    }
    
    
    /**
     * DELETE  /amembers/:id : delete the "id" amember.
     *
     * @param id the id of the amember to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/amembers/{id}")
    @Timed
    public ResponseEntity<Void> deleteAmember(@PathVariable Long id) {
        log.debug("REST request to delete Amember : {}", id);
        amemberRepository.delete(id);
        amemberSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/amembers?query=:query : search for the amember corresponding
     * to the query.
     *
     * @param query the query of the amember search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/amembers")
    @Timed
    public ResponseEntity<List<Amember>> searchAmembers(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Amembers for query {}", query);
        Page<Amember> page = amemberSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/amembers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
