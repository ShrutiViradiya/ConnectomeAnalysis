#Longitudinal Stream
#https://surfer.nmr.mgh.harvard.edu/fswiki/LongitudinalProcessing

#step1_before
#recon-all -all -s ngDE"${1}" -i /home/uoeh/Desktop/6month_follow/ngDE"${1}".nii

#step1_after
#recon-all -all -s ngaDE"${1}" -i /home/uoeh/Desktop/6month_follow/ngaDE"${1}".nii

#step2
recon-all -base temp_ngDE"${1}" -tp ngDE"${1}" -tp ngaDE"${1}" -all

#step3_before
recon-all -long ngDE"${1}" temp_ngDE"${1}" -all

#step3_after
recon-all -long ngaDE"${1}" temp_ngDE"${1}" -all

################################################################


#Longitudinal segmentation of hippocampal subfields
#longHippoSubfieldsT1.sh <baseID> [SubjectsDirectory]
