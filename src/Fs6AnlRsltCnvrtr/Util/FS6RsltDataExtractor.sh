WorkingDirectory=/media/DATA/kumadai/20161015_fs_vol
cd ${WorkingDirectory}
folder_names=$(ls)
#echo ${folder_names}

for folder_name in ${folder_names}
do
	echo ${folder_name}

	SUBJECTS_FLD=${WorkingDirectory}/${folder_name}/freesurfer_processed
	SUBJECT_FLD=${SUBJECTS_FLD}/${folder_name}
	OUTPUT_FOLDER=~/Desktop/RESULTS_20161124

	#右海馬
	cp ${SUBJECT_FLD}/mri/rh.hippoSfVolumes-T1.v10.txt $OUTPUT_FOLDER/${folder_name}_rh.hippoSfVolumes-T1.v10.txt

	#左海馬
	cp ${SUBJECT_FLD}/mri/lh.hippoSfVolumes-T1.v10.txt $OUTPUT_FOLDER/${folder_name}_lh.hippoSfVolumes-T1.v10.txt

	#aseg
	#asegstats2table --subdir=${SUBJECT_FLD}/stats --subjects ${folder_name} --meas volume -t $OUTPUT_FOLDER/${folder_name}_aseg_stats.txt
	cp ${SUBJECT_FLD}/stats/aseg.stats $OUTPUT_FOLDER/${folder_name}_aseg.stats

	#rh.aparc
	cp ${SUBJECT_FLD}/stats/rh.aparc.DKTatlas.stats $OUTPUT_FOLDER/${folder_name}_rh.aparc.DKTatlas.stats

	#lh.aparc
	cp ${SUBJECT_FLD}/stats/lh.aparc.DKTatlas.stats $OUTPUT_FOLDER/${folder_name}_lh.aparc.DKTatlas.stats
done
